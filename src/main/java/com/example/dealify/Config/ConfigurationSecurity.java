package com.example.dealify.Config;

import com.example.dealify.Service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ConfigurationSecurity {
    private final MyUserDetailsService myUserDetailsService;

    public ConfigurationSecurity(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/customer/register",
                        "/api/v1/category/get-all-categories",
                        "/api/v1/vendor/register",
                        "/api/v1/vendor/get-vendor-open-deals/vendor-id/{vendor-id}",
                        "/api/v1/category/get-category-by-name/{name}",
                        "/api/v1/vendor/get-vendor-profile/vendor-id/{vendor-id}",
                        "/api/v1/customer/get-customer-reviews/customer-id/{customer-id}",
                        "/api/v1/customer/get-product-by-id/product-id/{product-id}",
                        "/api/v1/customer/get-product-images/product-id/{product-id}",
                        "/api/v1/customer/get-products-by-vendor/vendor-id/{vendor-id}",
                        "/api/v1/customer/get-product-reviews/product-id/{product-id}",
                        "/api/v1/product/get-all-products",
                        "/api/v1/product/get-products-by-category/category-name/{category-name}",
                        "/api/v1/product/get-product-open-deals/{product-id}",
                        "/api/v1/product/get-products-by-price-range/{min}/{max}",
                        "/api/v1/vendor-profile/get-vendor-reviews/vendor-id/{vendor-id}",
                        "/api/v1/vendor-profile/get-vendors-by-city/{city}").permitAll()

                .requestMatchers("/api/v1/customer/get-all-users",
                        "/api/v1/inventory/get-all-inventories",
                        "/api/v1/category/add-category",
                        "/api/v1/category/update-category/category-id/{category-id}",
                        "/api/v1/category/delete-category/category-id/{category-id}",
                        "/api/v1/vendor/activate/vendor-id/{vendor-id}",
                        "/api/v1/return-request/get-all-return-requests",
                        "/api/v1/customer-review/get-All-customers-reviews").hasAuthority("ADMIN")

                .requestMatchers("/api/v1/vendor/update-vendor",
                        "/api/v1/vendor/delete-vendor",
                        "/api/v1/vendor/get-my-inventory",
                        "/api/v1/vendor/get-vendor-return-requests",
                        "/api/v1/vendor/get-vendor-black-list",
                        "/api/v1/vendor/add-customer-to-blacklist/customer-id/{customer-id}",
                        "/api/v1/vendor/remove-customer-from-blacklist/{customer-id}",
                        "/api/v1/vendor/get-vendor-blacklist-pardon-request",
                        "/api/v1/vendor/approve-pardon-request/{pardon-request-id}",
                        "/api/v1/vendor/reject-pardon-request/{pardon-request-id}",
                        "/api/v1/vendor/update-product-images/product-id/{product-id}",
                        "/api/v1/vendor/accept-return-request/{return-request-id}",
                        "/api/v1/vendor/reject-return-request/{return-request-id}",
                        "/api/v1/vendor-profile/update-vendor-profile",
                        "/api/v1/product/add-product",
                        "/api/v1/product/update/product-id/{product-id}",
                        "/api/v1/product/delete/product-id/{product-id}",
                        "/api/v1/product/low-stock/{stock-limit}").hasAuthority("VENDOR")

                .requestMatchers("/api/v1/customer/update-customer"
                        , "/api/v1/customer/delete-customer"
                        , "/api/v1/customer/get-customer-by-id/user-id/{id}"
                        , "/api/v1/customer/create-a-deal/product-id/{product-id}"
                        , "/api/v1/customer/get-customer-open-deals"
                        , "/api/v1/customer/get-customer-completed-deals"
                        , "/api/v1/customer/get-deals-by-product-category/category-name/{category-name}"
                        , "/api/v1/customer/join-a-deal/deal-id/{deal-id}"
                        , "/api/v1/customer/update-quantity-to-buy/customer-deal-id/{customer-deal-id}"
                        , "/api/v1/customer/leave-a-deal/customer-deal-id/{customer-deal-id}"
                        , "/api/v1/customer/get-customer-favorites"
                        , "/api/v1/customer/add-product-to-favorite/product-id/{product-id}"
                        , "/api/v1/customer/remove-product-from-favorite/product-id/{product-id}"
                        , "/api/v1/customer/invite-to-deal/deal-id/{deal-id}/invitee-customer-id/{invitee-customer-id}"
                        , "/api/v1/customer/review-a-customer/reviewed-id/{reviewed-id}"
                        , "/api/v1/customer/update-a-customer-review/review-id/{review-id}"
                        , "/api/v1/customer/delete-a-customer-review/review-id/{review-id}"
                        , "/api/v1/customer/create-return-request/product-id/{productId}/deal-id/{dealId}"
                        , "/api/v1/customer/get-customer-return-requests"
                        , "/api/v1/customer/review-a-product/product-id/{product-id}"
                        , "/api/v1/customer/update-a-product-review/product-review-id/{product-review-id}"
                        , "/api/v1/customer/get-customer-pardon-requests"
                        , "/api/v1/customer/request-a-pardon/vendor-id/{vendor-id}"
                        , "/api/v1/customer/update-a-pardon-request/pardon-request-id/{pardon-request-id}"
                        , "/api/v1/customer/pay/deal-id/{deal-id}"
                        , "/api/v1/customer/pay-with-points/deal-id/{deal-id}"
                        , "/api/v1/customer/decline-to-pay/deal-id/{deal-id}"
                        , "/api/v1/customer-profile/update/customer"
                        , "/api/v1/vendor-review/add-review/{product-id}"
                        , "/api/v1/vendor-review/update-review/{review-id}"
                        , "/api/v1/vendor-review/delete-review/{review-id}").hasAuthority("CUSTOMER")
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/api/v1/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .httpBasic();
        return http.build();
    }
}