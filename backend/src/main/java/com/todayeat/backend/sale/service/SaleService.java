package com.todayeat.backend.sale.service;

import com.todayeat.backend._common.response.error.ErrorType;
import com.todayeat.backend._common.response.error.exception.BusinessException;
import com.todayeat.backend._common.util.SecurityUtil;
import com.todayeat.backend.menu.entitiy.Menu;
import com.todayeat.backend.menu.repository.MenuRepository;
import com.todayeat.backend.sale.dto.request.CreateSaleListRequest;
import com.todayeat.backend.sale.dto.request.CreateSaleRequest;
import com.todayeat.backend.sale.dto.request.UpdateSaleStatusRequest;
import com.todayeat.backend.sale.entity.Sale;
import com.todayeat.backend.sale.mapper.SaleMapper;
import com.todayeat.backend.sale.repository.SaleRepository;
import com.todayeat.backend.seller.entity.Seller;
import com.todayeat.backend.seller.repository.SellerRepository;
import com.todayeat.backend.store.entity.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SaleService {

    private final SecurityUtil securityUtil;
    private final MenuRepository menuRepository;
    private final SellerRepository sellerRepository;
    private final SaleRepository saleRepository;

    @Transactional
    public void create(CreateSaleListRequest request) {

        Seller seller = securityUtil.getSeller();

        // 판매자의 가게가 맞는지 확인, 가게 존재 여부 확인
        Store store = validateStoreAndSeller(seller, request.getStoreId());

        List<Sale> saleList = new ArrayList<>();

        for(CreateSaleRequest createSaleRequest : request.getSaleList()) {
            // 해당 메뉴의 존재 여부 확인 및 가게에 있는 메뉴인지 확인
            Menu menu = validateMenuAndStore(createSaleRequest.getMenuId(), store);

            saleList.add(
                    SaleMapper.INSTANCE
                    .createSaleReqeustToSale(createSaleRequest,
                            getDiscountRate(menu.getOriginalPrice(), createSaleRequest.getSellPrice()),
                            false, 0, store, menu)
            );
        }

        saleRepository.saveAll(saleList);
    }

    @Transactional
    public void updateStatus(Long saleId, UpdateSaleStatusRequest request) {

        Seller seller = securityUtil.getSeller();

        // todo 쿼리 세개 나가는데 성능 리펙토링 해보기
        // 판매자의 가게가 맞는지 확인, 가게 존재 여부 확인
        Store store = validateStoreAndSeller(seller, request.getStoreId());

        // 해당 메뉴의 존재 여부 확인 및 가게에 있는 메뉴인지 확인
        Menu menu = validateMenuAndStore(request.getMenuId(), store);

        // 해당 가게의 메뉴인 판매가 있는지 확인
        Sale sale = saleRepository
                .findByIdAndStoreIdAndMenuIdAndDeletedAtIsNullAndStoreDeletedAtIsNullAndMenuDeletedAtIsNull(
                        saleId,
                        store.getId(),
                        menu.getId()
                )
                .orElseThrow(() -> new BusinessException(ErrorType.SALE_STATUS_UPDATE_FAIL));

        sale.updateStatus();
    }

    // 판매자의 가게가 맞는지 확인, 가게 존재 여부 확인
    private Store validateStoreAndSeller(Seller seller, Long storeId) {

        return sellerRepository.findByIdAndStoreIdAndDeletedAtIsNullAndStoreDeletedAtIsNull(seller.getId(), storeId)
                .orElseThrow(() -> new BusinessException(ErrorType.STORE_NOT_FOUND)).getStore();
    }

    // 해당 메뉴의 존재 여부 확인 및 가게에 있는 메뉴인지 확인
    private Menu validateMenuAndStore(Long menuId, Store store) {

        return menuRepository.findByIdAndStoreAndDeletedAtIsNullAndStoreDeletedAtIsNull(menuId, store)
                .orElseThrow(() -> new BusinessException(ErrorType.MENU_NOT_FOUND));
    }

    // 할인률 계산
    private Integer getDiscountRate(Integer originalPrice, Integer sellPrice) {

        // 판매가가 원가보다 큰 경우 예외
        if(originalPrice < sellPrice) {
            throw new BusinessException(ErrorType.MENU_CREATE_FAIL);
        }

        if (originalPrice == 0) {
            throw new BusinessException(ErrorType.MENU_GET_DISCOUNT_RATE_FAIL);
        }

        return 100 * (originalPrice - sellPrice) / originalPrice;
    }
}
