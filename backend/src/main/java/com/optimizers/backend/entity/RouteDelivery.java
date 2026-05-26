package com.optimizers.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "route_delivery",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_route_delivery", columnNames = {"route_id", "delivery_id"}),
                @UniqueConstraint(name = "uq_route_stop_sequence", columnNames = {"route_id", "stop_sequence"})
        }
)
public class RouteDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_delivery_id")
    private Integer routeDeliveryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Column(name = "stop_sequence", nullable = false)
    private Integer stopSequence;

    @Column(name = "predicted_eta")
    private LocalDateTime predictedEta;

    @Column(name = "estimated_arrival_time")
    private LocalDateTime estimatedArrivalTime;

    @Column(name = "actual_arrival_time")
    private LocalDateTime actualArrivalTime;

    @Column(name = "stop_status", length = 30)
    private String stopStatus;

    public RouteDelivery() {
    }

    public Integer getRouteDeliveryId() {
        return routeDeliveryId;
    }

    public void setRouteDeliveryId(Integer routeDeliveryId) {
        this.routeDeliveryId = routeDeliveryId;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Integer getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(Integer stopSequence) {
        this.stopSequence = stopSequence;
    }

    public LocalDateTime getPredictedEta() {
        return predictedEta;
    }

    public void setPredictedEta(LocalDateTime predictedEta) {
        this.predictedEta = predictedEta;
    }

    public LocalDateTime getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(LocalDateTime estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public LocalDateTime getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setActualArrivalTime(LocalDateTime actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }

    public String getStopStatus() {
        return stopStatus;
    }

    public void setStopStatus(String stopStatus) {
        this.stopStatus = stopStatus;
    }

    @PrePersist
    public void prePersist() {
        if (this.stopStatus == null) {
            this.stopStatus = "PENDING";
        }
    }
}