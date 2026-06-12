CREATE TABLE service_suppliers (
    service_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,

    PRIMARY KEY (service_id, supplier_id),

    CONSTRAINT fk_service_suppliers_service
        FOREIGN KEY (service_id)
        REFERENCES services(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_service_suppliers_supplier
        FOREIGN KEY (supplier_id)
        REFERENCES suppliers(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_service_suppliers_service_id
    ON service_suppliers(service_id);

CREATE INDEX idx_service_suppliers_supplier_id
    ON service_suppliers(supplier_id);