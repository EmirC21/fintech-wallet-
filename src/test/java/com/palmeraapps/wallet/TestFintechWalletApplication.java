package com.palmeraapps.wallet;

import org.springframework.boot.SpringApplication;

public class TestFintechWalletApplication {

	public static void main(String[] args) {
		SpringApplication.from(FintechWalletApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
