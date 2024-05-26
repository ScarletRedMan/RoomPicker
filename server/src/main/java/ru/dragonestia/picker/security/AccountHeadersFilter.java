package ru.dragonestia.picker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.model.account.Account;
import ru.dragonestia.picker.model.account.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter
@Component
@RequiredArgsConstructor
public class AccountHeadersFilter implements Filter {

    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var data = new AccountData(account.getId().getValue(), new ArrayList<>(account.getAuthorities()), account.isLocked());

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("X-RoomPicker-Account", objectMapper.writeValueAsString(data));

        chain.doFilter(request, response);
    }

    public record AccountData(String id, List<Permission> permissions, boolean locked) {}
}
