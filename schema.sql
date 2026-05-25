-- ============================================================
-- SMART LOGISTICS + INVENTORY OPTIMIZER FULL SYSTEM SCHEMA
-- Combines:
-- 1. Vehicles + Drivers + Deliveries + Routes
-- 2. Warehouses + Inventory + Reorder Alerts + AI tables
-- ============================================================
-- ============================================================
-- 1. WAREHOUSE
-- ============================================================

CREATE TABLE warehouse (
    warehouse_id         SERIAL PRIMARY KEY,
    name                 VARCHAR(150) NOT NULL,
    address              TEXT,
    latitude             NUMERIC(10,7),
    longitude            NUMERIC(10,7),
    manager_name         VARCHAR(100),
    contact_number       VARCHAR(20),
    total_capacity_sqm   NUMERIC(10,2),
    status               VARCHAR(30) DEFAULT 'ACTIVE',
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 2. VEHICLE
-- ============================================================

CREATE TABLE vehicle (
    vehicle_id           SERIAL PRIMARY KEY,
    vehicle_number       VARCHAR(50) NOT NULL UNIQUE,
    vehicle_type         VARCHAR(50) NOT NULL,
    capacity_kg          NUMERIC(10,2) NOT NULL,
    max_volume           NUMERIC(10,2),
    fuel_type            VARCHAR(30),
    current_status       VARCHAR(30) DEFAULT 'AVAILABLE',
    current_latitude     NUMERIC(10,7),
    current_longitude    NUMERIC(10,7),
    current_warehouse_id INT,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vehicle_warehouse
        FOREIGN KEY (current_warehouse_id)
        REFERENCES warehouse(warehouse_id)
        ON DELETE SET NULL
);

-- ============================================================
-- 3. DRIVER
-- ============================================================

CREATE TABLE driver (
    driver_id            SERIAL PRIMARY KEY,
    driver_name          VARCHAR(100) NOT NULL,
    phone                VARCHAR(20),
    license_no           VARCHAR(50) NOT NULL UNIQUE,
    status               VARCHAR(30) DEFAULT 'ACTIVE',
    vehicle_id           INT UNIQUE,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_driver_vehicle
        FOREIGN KEY (vehicle_id)
        REFERENCES vehicle(vehicle_id)
        ON DELETE SET NULL
);

-- ============================================================
-- 4. DELIVERY
-- ============================================================

CREATE TABLE delivery (
    delivery_id          SERIAL PRIMARY KEY,
    warehouse_id         INT NOT NULL,
    customer_name        VARCHAR(100) NOT NULL,
    contact_number       VARCHAR(20),
    delivery_address     TEXT NOT NULL,
    latitude             NUMERIC(10,7) NOT NULL,
    longitude            NUMERIC(10,7) NOT NULL,
    package_weight       NUMERIC(10,2) DEFAULT 0,
    package_volume       NUMERIC(10,2),
    priority             VARCHAR(20) DEFAULT 'MEDIUM',
    status               VARCHAR(30) DEFAULT 'PENDING',
    requested_date       TIMESTAMP,
    time_window_start    TIMESTAMP,
    time_window_end      TIMESTAMP,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_delivery_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouse(warehouse_id)
        ON DELETE RESTRICT
);

-- ============================================================
-- 5. ROUTE
-- ============================================================

CREATE TABLE route (
    route_id               SERIAL PRIMARY KEY,
    vehicle_id             INT NOT NULL,
    driver_id              INT,
    route_date             DATE NOT NULL,
    start_warehouse_id     INT,
    start_location         VARCHAR(150),
    end_location           VARCHAR(150),
    total_distance_km      NUMERIC(10,2),
    estimated_duration_min NUMERIC(10,2),
    predicted_cost         NUMERIC(10,2),
    predicted_delay_risk   NUMERIC(5,2),
    optimization_score     NUMERIC(5,2),
    route_status           VARCHAR(30) DEFAULT 'PLANNED',
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_route_vehicle
        FOREIGN KEY (vehicle_id)
        REFERENCES vehicle(vehicle_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_route_driver
        FOREIGN KEY (driver_id)
        REFERENCES driver(driver_id)
        ON DELETE SET NULL,

    CONSTRAINT fk_route_start_warehouse
        FOREIGN KEY (start_warehouse_id)
        REFERENCES warehouse(warehouse_id)
        ON DELETE SET NULL
);

-- ============================================================
-- 6. ROUTE DELIVERY
-- ============================================================

CREATE TABLE route_delivery (
    route_delivery_id       SERIAL PRIMARY KEY,
    route_id                INT NOT NULL,
    delivery_id             INT NOT NULL,
    stop_sequence           INT NOT NULL,
    predicted_eta           TIMESTAMP,
    estimated_arrival_time  TIMESTAMP,
    actual_arrival_time     TIMESTAMP,
    stop_status             VARCHAR(30) DEFAULT 'PENDING',

    CONSTRAINT fk_route_delivery_route
        FOREIGN KEY (route_id)
        REFERENCES route(route_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_route_delivery_delivery
        FOREIGN KEY (delivery_id)
        REFERENCES delivery(delivery_id)
        ON DELETE CASCADE,

    CONSTRAINT uq_route_delivery UNIQUE (route_id, delivery_id),
    CONSTRAINT uq_route_stop_sequence UNIQUE (route_id, stop_sequence)
);

-- ============================================================
-- 7. INVENTORY ITEM
-- product catalogue per warehouse context
-- ============================================================

CREATE TABLE inventory_item (
    item_id               SERIAL PRIMARY KEY,
    warehouse_id          INT NOT NULL,
    sku                   VARCHAR(100) NOT NULL UNIQUE,
    item_name             VARCHAR(200) NOT NULL,
    category              VARCHAR(100),
    unit                  VARCHAR(30) NOT NULL,
    unit_weight_kg        NUMERIC(10,3),
    unit_volume           NUMERIC(10,3),
    unit_price            NUMERIC(12,2),
    status                VARCHAR(30) DEFAULT 'ACTIVE',
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_item_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouse(warehouse_id)
        ON DELETE RESTRICT
);

-- ============================================================
-- 8. INVENTORY STOCK
-- ============================================================

CREATE TABLE inventory_stock (
    stock_id              SERIAL PRIMARY KEY,
    item_id               INT NOT NULL UNIQUE,
    warehouse_id          INT NOT NULL,
    quantity_on_hand      NUMERIC(12,3) NOT NULL DEFAULT 0,
    reserved_quantity     NUMERIC(12,3) NOT NULL DEFAULT 0,
    available_quantity    NUMERIC(12,3) GENERATED ALWAYS AS
                            (quantity_on_hand - reserved_quantity) STORED,
    reorder_level         NUMERIC(12,3) NOT NULL,
    reorder_quantity      NUMERIC(12,3) NOT NULL,
    max_stock_level       NUMERIC(12,3),
    last_updated          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_stock_item
        FOREIGN KEY (item_id)
        REFERENCES inventory_item(item_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_stock_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouse(warehouse_id)
        ON DELETE RESTRICT
);

-- ============================================================
-- 9. DELIVERY ITEM
-- links deliveries with inventory items
-- ============================================================

CREATE TABLE delivery_item (
    delivery_item_id      SERIAL PRIMARY KEY,
    delivery_id           INT NOT NULL,
    item_id               INT NOT NULL,
    quantity              NUMERIC(12,3) NOT NULL,
    allocated_stock_id    INT,
    unit_price            NUMERIC(12,2),
    total_price           NUMERIC(12,2) GENERATED ALWAYS AS
                            (quantity * COALESCE(unit_price, 0)) STORED,

    CONSTRAINT fk_delivery_item_delivery
        FOREIGN KEY (delivery_id)
        REFERENCES delivery(delivery_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_delivery_item_item
        FOREIGN KEY (item_id)
        REFERENCES inventory_item(item_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_delivery_item_stock
        FOREIGN KEY (allocated_stock_id)
        REFERENCES inventory_stock(stock_id)
        ON DELETE SET NULL
);

-- ============================================================
-- 10. STOCK TRANSACTION
-- ============================================================

CREATE TABLE stock_transaction (
    transaction_id        SERIAL PRIMARY KEY,
    stock_id              INT NOT NULL,
    item_id               INT NOT NULL,
    warehouse_id          INT NOT NULL,
    delivery_id           INT,
    transaction_type      VARCHAR(50) NOT NULL,
    quantity              NUMERIC(12,3) NOT NULL,
    quantity_before       NUMERIC(12,3) NOT NULL,
    quantity_after        NUMERIC(12,3) NOT NULL,
    reference_type        VARCHAR(50),
    reference_id          INT,
    performed_by          VARCHAR(100),
    notes                 TEXT,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_txn_stock
        FOREIGN KEY (stock_id)
        REFERENCES inventory_stock(stock_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_txn_item
        FOREIGN KEY (item_id)
        REFERENCES inventory_item(item_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_txn_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouse(warehouse_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_txn_delivery
        FOREIGN KEY (delivery_id)
        REFERENCES delivery(delivery_id)
        ON DELETE SET NULL
);

-- ============================================================
-- 11. REORDER ALERT
-- ============================================================

CREATE TABLE reorder_alert (
    alert_id                SERIAL PRIMARY KEY,
    stock_id                INT NOT NULL,
    item_id                 INT NOT NULL,
    warehouse_id            INT NOT NULL,
    alert_type              VARCHAR(50) NOT NULL DEFAULT 'LOW_STOCK',
    current_stock           NUMERIC(12,3) NOT NULL,
    reorder_level           NUMERIC(12,3) NOT NULL,
    suggested_reorder_qty   NUMERIC(12,3),
    predicted_demand_7d     NUMERIC(12,3),
    severity                VARCHAR(20) DEFAULT 'MEDIUM',
    status                  VARCHAR(30) DEFAULT 'OPEN',
    triggered_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at             TIMESTAMP,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_alert_stock
        FOREIGN KEY (stock_id)
        REFERENCES inventory_stock(stock_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_alert_item
        FOREIGN KEY (item_id)
        REFERENCES inventory_item(item_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_alert_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouse(warehouse_id)
        ON DELETE RESTRICT
);

-- ============================================================
-- 12. DEMAND FORECAST
-- ============================================================

CREATE TABLE demand_forecast (
    forecast_id           SERIAL PRIMARY KEY,
    item_id               INT NOT NULL,
    warehouse_id          INT NOT NULL,
    forecast_date         DATE NOT NULL,
    predicted_demand      NUMERIC(12,3) NOT NULL,
    confidence_score      NUMERIC(5,4),
    season_pattern        VARCHAR(50),
    model_version         VARCHAR(50),
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_forecast_item
        FOREIGN KEY (item_id)
        REFERENCES inventory_item(item_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_forecast_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouse(warehouse_id)
        ON DELETE RESTRICT,

    CONSTRAINT uq_forecast_item_date
        UNIQUE (item_id, warehouse_id, forecast_date, model_version)
);

-- ============================================================
-- 13. ANOMALY LOG
-- ============================================================

CREATE TABLE anomaly_log (
    anomaly_id            SERIAL PRIMARY KEY,
    item_id               INT,
    warehouse_id          INT,
    delivery_id           INT,
    anomaly_type          VARCHAR(100) NOT NULL,
    description           TEXT,
    deviation_score       NUMERIC(8,4),
    status                VARCHAR(30) DEFAULT 'OPEN',
    detected_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at           TIMESTAMP,

    CONSTRAINT fk_anomaly_item
        FOREIGN KEY (item_id)
        REFERENCES inventory_item(item_id)
        ON DELETE SET NULL,

    CONSTRAINT fk_anomaly_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouse(warehouse_id)
        ON DELETE SET NULL,

    CONSTRAINT fk_anomaly_delivery
        FOREIGN KEY (delivery_id)
        REFERENCES delivery(delivery_id)
        ON DELETE SET NULL
);

-- ============================================================
-- INDEXES
-- ============================================================

CREATE INDEX idx_vehicle_status         ON vehicle(current_status);
CREATE INDEX idx_vehicle_warehouse      ON vehicle(current_warehouse_id);

CREATE INDEX idx_delivery_warehouse     ON delivery(warehouse_id);
CREATE INDEX idx_delivery_status        ON delivery(status);
CREATE INDEX idx_delivery_priority      ON delivery(priority);
CREATE INDEX idx_delivery_requested     ON delivery(requested_date);

CREATE INDEX idx_route_vehicle          ON route(vehicle_id);
CREATE INDEX idx_route_driver           ON route(driver_id);
CREATE INDEX idx_route_date            ON route(route_date);
CREATE INDEX idx_route_status          ON route(route_status);

CREATE INDEX idx_route_delivery_route   ON route_delivery(route_id);
CREATE INDEX idx_route_delivery_delivery ON route_delivery(delivery_id);

CREATE INDEX idx_item_warehouse         ON inventory_item(warehouse_id);
CREATE INDEX idx_item_sku               ON inventory_item(sku);
CREATE INDEX idx_item_category          ON inventory_item(category);

CREATE INDEX idx_stock_warehouse        ON inventory_stock(warehouse_id);
CREATE INDEX idx_stock_reorder          ON inventory_stock(reorder_level);

CREATE INDEX idx_delivery_item_delivery ON delivery_item(delivery_id);
CREATE INDEX idx_delivery_item_item     ON delivery_item(item_id);

CREATE INDEX idx_txn_item               ON stock_transaction(item_id);
CREATE INDEX idx_txn_warehouse          ON stock_transaction(warehouse_id);
CREATE INDEX idx_txn_type               ON stock_transaction(transaction_type);
CREATE INDEX idx_txn_created            ON stock_transaction(created_at);
CREATE INDEX idx_txn_delivery           ON stock_transaction(delivery_id);

CREATE INDEX idx_alert_status           ON reorder_alert(status);
CREATE INDEX idx_alert_severity         ON reorder_alert(severity);
CREATE INDEX idx_alert_warehouse        ON reorder_alert(warehouse_id);
CREATE INDEX idx_alert_triggered        ON reorder_alert(triggered_at);

CREATE INDEX idx_forecast_date          ON demand_forecast(forecast_date);
CREATE INDEX idx_forecast_item          ON demand_forecast(item_id);

CREATE INDEX idx_anomaly_type           ON anomaly_log(anomaly_type);
CREATE INDEX idx_anomaly_status         ON anomaly_log(status);
CREATE INDEX idx_anomaly_detected       ON anomaly_log(detected_at);

-- ============================================================
-- TRIGGER: auto-update inventory_stock.last_updated
-- ============================================================

CREATE OR REPLACE FUNCTION update_stock_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.last_updated = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_stock_updated
    BEFORE UPDATE ON inventory_stock
    FOR EACH ROW
    EXECUTE FUNCTION update_stock_timestamp();

-- ============================================================
-- VIEW: low stock items
-- ============================================================

CREATE VIEW v_low_stock_items AS
SELECT
    s.stock_id,
    w.name AS warehouse_name,
    i.sku,
    i.item_name,
    i.category,
    s.quantity_on_hand,
    s.reserved_quantity,
    s.available_quantity,
    s.reorder_level,
    s.reorder_quantity,
    CASE
        WHEN s.available_quantity <= 0 THEN 'OUT_OF_STOCK'
        WHEN s.available_quantity <= s.reorder_level * 0.5 THEN 'CRITICAL'
        ELSE 'LOW'
    END AS stock_status
FROM inventory_stock s
JOIN inventory_item i
    ON i.item_id = s.item_id
JOIN warehouse w
    ON w.warehouse_id = s.warehouse_id
WHERE s.available_quantity <= s.reorder_level
  AND i.status = 'ACTIVE'
  AND w.status = 'ACTIVE';