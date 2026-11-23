CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    step BIGINT  NOT NULL,
    "type" VARCHAR(20) NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    name_orig VARCHAR(50) NOT NULL,
    oldbalance_org NUMERIC(15, 2) NOT NULL,
    newbalance_orig NUMERIC(15, 2) NOT NULL,
    name_dest VARCHAR(50) NOT NULL,
    oldbalance_dest NUMERIC(15, 2) NOT NULL,
    newbalance_dest NUMERIC(15, 2) NOT NULL,
    is_fraud BOOLEAN NOT NULL,
    is_flagged_fraud BOOLEAN NOT NULL
);

/*
INSERT INTO transactions (
    step,
    type,
    amount,
    name_orig,
    oldbalance_org,
    newbalance_orig,
    name_dest,
    oldbalance_dest,
    newbalance_dest,
    is_fraud,
    is_flagged_fraud
) VALUES (
             1,
             'PAYMENT',
             9839.64,
             'C1231006815',
             170136.00,
             160296.36,
             'M1979787155',
             0.00,
             0.00,
             FALSE,
             FALSE
         );
*/