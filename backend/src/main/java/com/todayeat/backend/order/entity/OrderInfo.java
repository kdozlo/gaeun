package com.todayeat.backend.order.entity;

import com.todayeat.backend._common.entity.BaseTime;
import com.todayeat.backend.consumer.entity.Consumer;
import com.todayeat.backend.review.entity.Review;
import com.todayeat.backend.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE order_info SET deleted_at = CONVERT_TZ(NOW(), '+00:00', '+09:00') WHERE order_info_id = ?")
public class OrderInfo extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_info_id")
    private Long id;

    @Column(nullable = true)
    private String paymentId;

    @Column(nullable = false, length = 50)
    private String orderNo;

    @Column(nullable = false)
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'UNPAID'")
    private OrderInfoStatus status;

    @Column(nullable = true)
    private Integer takenTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id", referencedColumnName = "consumer_id")
    private Consumer consumer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", referencedColumnName = "store_id")
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @OneToMany(mappedBy = "orderInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderInfoItem> orderInfoItemList = new ArrayList<>();

    @Builder
    private OrderInfo(String paymentId, String orderNo, Integer totalPrice, OrderInfoStatus status, Integer takenTime, Consumer consumer, Store store, Review review) {
        this.paymentId = paymentId;
        this.orderNo = orderNo;
        this.totalPrice = totalPrice;
        this.status = status;
        this.takenTime = takenTime;
        this.consumer = consumer;
        this.store = store;
        this.review = review;
    }

    public static OrderInfo of(String orderNo, Integer totalPrice, Consumer consumer, Store store) {
        return builder()
                .orderNo(orderNo)
                .totalPrice(totalPrice)
                .consumer(consumer)
                .store(store)
                .build();
    }

    public void updatePaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void updateStatus(OrderInfoStatus status) {
        this.status = status;
    }

    public void updateTakenTime(Integer takenTime) {
        this.takenTime = takenTime;
    }
}
