CREATE TABLE car (
                     id          BIGSERIAL PRIMARY KEY,
                     brand       VARCHAR(100)   NOT NULL,
                     model       VARCHAR(100)   NOT NULL,
                     price       NUMERIC(12, 2) NOT NULL
);

CREATE TABLE person (
                        id               BIGSERIAL PRIMARY KEY,
                        name             VARCHAR(100)  NOT NULL,
                        age              INTEGER       NOT NULL,
                        has_driver_license BOOLEAN     NOT NULL DEFAULT FALSE,
                        car_id           BIGINT        REFERENCES car (id)
);