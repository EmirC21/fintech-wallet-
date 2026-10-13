package com.palmeraapps.wallet.wallet.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.palmeraapps.wallet.customer.domain.Customer;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wallets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String walletNumber;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private WalletStatus status;

    private Instant createdAt;

    public Wallet(Customer customer, Currency currency, String walletNumber) {
        this.id = UUID.randomUUID();
        this.customer = customer;
        this.walletNumber = walletNumber;
        this.balance = BigDecimal.ZERO;
        this.createdAt = Instant.now();
        this.status = WalletStatus.ACTIVE;
        this.currency = currency;
    }

    public void freeze() {
        if (status == WalletStatus.CLOSED) {
            throw new IllegalStateException("closed wallet cannot be frozen");
        }
        this.status = WalletStatus.FROZEN;
    }

    public void activate() {
        if (status == WalletStatus.CLOSED) {
            throw new IllegalStateException("Closed wallet cannot be activated");
        }
        this.status = WalletStatus.ACTIVE;
    }

    public void close() {
        if (status == WalletStatus.CLOSED) {
            throw new IllegalStateException("Wallet is already closed");
        }
        if (balance.signum() != 0) {
            throw new IllegalStateException("Wallet with non-zero balance cannot be closed");
        }
        this.status = WalletStatus.CLOSED;
    }

}
