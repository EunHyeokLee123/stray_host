package com.strayanimal.schedulerservice.api.repository.custom.Impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.strayanimal.schedulerservice.api.dto.CategoryDto;
import com.strayanimal.schedulerservice.api.dto.PetListResDto;
import com.strayanimal.schedulerservice.api.entity.QPetColor;
import com.strayanimal.schedulerservice.api.entity.QStrayAnimalEntity;
import com.strayanimal.schedulerservice.api.repository.custom.AnimalsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

@RequiredArgsConstructor
public class AnimalsRepositoryImpl implements AnimalsRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final QStrayAnimalEntity strayAnimal = QStrayAnimalEntity.strayAnimalEntity;
    private static final QPetColor petColor = QPetColor.petColor;

    @Override
    public Page<PetListResDto> getRightAnimals(CategoryDto dto, int page) {

        int pageSize = getPageSize(dto.getDevice());

        List<PetListResDto> mid = queryFactory.select(
                        Projections.constructor(PetListResDto.class,
                                strayAnimal.desertionNo,
                                strayAnimal.upKindNm,
                                strayAnimal.kindNm,
                                strayAnimal.age,
                                strayAnimal.popfile1,
                                strayAnimal.sexCd,
                                strayAnimal.careTel,
                                strayAnimal.careAddr,
                                strayAnimal.happenDt,
                                strayAnimal.neuterYn,
                                strayAnimal.colorCd
                        ))
                .from(strayAnimal)
                .join(petColor)
                .on(strayAnimal.desertionNo.eq(petColor.desertionNo))
                .where(builderCondition(dto))
                .orderBy(strayAnimal.happenDt.desc(), strayAnimal.desertionNo.desc())
                .offset((long) page * pageSize)       // 페이지 번호 기반 오프셋 적용
                .limit(pageSize)      // 한 페이지 크기 제한
                .fetch();

        // 전체 데이터 개수 조회 (페이징을 위해 필요)
        Long count = queryFactory
                .select(strayAnimal.count())
                .from(strayAnimal)
                .join(petColor)
                .on(strayAnimal.desertionNo.eq(petColor.desertionNo))
                .where(builderCondition(dto))
                .fetchOne();

        Pageable pageable = PageRequest.of(
                page,      // 0부터 시작
                pageSize          // 페이지당 아이템 수
        );

        // Page 객체로 변환하여 반환
        return new PageImpl<>(
                mid,
                pageable,
                count == null ? 0L : count.longValue()
        );
    }

    // 검색 조건(QueryDSL)을 구성하는 메서드
    private BooleanBuilder builderCondition(CategoryDto Dto) {
        BooleanBuilder builder = new BooleanBuilder();

        // 보호소 주소 검색 (시/도 단위부터 검색 가능)
        if (Dto.getRegion() != null && !Dto.getRegion().isBlank()) {
            builder.and(strayAnimal.careAddr.startsWithIgnoreCase(Dto.getRegion()));
        }

        // 축종명(개/고양이 등) 필터
        if (Dto.getKind() != null && !Dto.getKind().isBlank()) {
            builder.and(strayAnimal.upKindNm.eq(Dto.getKind()));
        }

        // 색상
        if (Dto.getColor() != null && !Dto.getColor().isEmpty()) {
            String normalized = makeNormalizedColorLine(Dto.getColor());
            builder.and(petColor.color.eq(normalized));
        }

        return builder;
    }

    private String makeNormalizedColorLine(List<String> input) {
        return input.stream()
                .sorted()
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }

    private int getPageSize(int device) {
        if(device == 0) {
            return 9;
        }
        else if(device == 1) {
            return 8;
        }
        else if(device == 2) {
            return 6;
        }
        else {
            throw new IllegalArgumentException("device must be 0 or 1");
        }

    }

}
