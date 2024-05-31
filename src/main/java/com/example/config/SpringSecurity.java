package com.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurity {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/js/**", "/css/**", "/fonts/**", "/images/**", "/static/**")
                .permitAll()
                .requestMatchers("/Admin/users",
                        "/Admin-News",
                        "/Admin-All-Artist",
                        "/Admin-All-Tracks",
                        "/track/createOrEditTrackPage",
                        "/artist/createOrEditArtistPage",
                        "artist/{artistId}/createOrEditAlbumPage",
                        "/artist/{artistId}/playlist/{playlistId}/addTrack",
                        "/Admin")
                .hasAuthority("Администратор")
                .requestMatchers("/setting")
                .authenticated()
                .anyRequest()
                .permitAll()
                .and()
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/login?error=true")
                                .permitAll()
                                .and()
                )
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login?expired=true")
                .and()
                .and()
                .rememberMe()
                .key("my-super-secret-key")
                .tokenValiditySeconds(604800)
                .and()
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                )
                .exceptionHandling()
                .accessDeniedPage("/");
        return http.build();
    }
}