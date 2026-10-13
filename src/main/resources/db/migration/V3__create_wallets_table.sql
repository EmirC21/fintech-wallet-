CREATE TABLE wallets (
      id UUID PRIMARY KEY,   
      customer_id UUID NOT NULL REFERENCES customers(id),
      wallet_number VARCHAR(20) NOT NULL UNIQUE, 
      currency VARCHAR(3) NOT NULL CHECK (currency IN ( 'TRY' , 'USD' , 'EUR')),
      balance NUMERIC(19, 4) NOT NULL DEFAULT 0 CHECK (balance >= 0),
      status VARCHAR(10) NOT NULL DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE', 'FROZEN','CLOSED' )),
      created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
      UNIQUE (customer_id, currency)      
);

