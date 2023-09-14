package com.example.bookme.web;

import com.example.bookme.config.PageableConstants;
import com.example.bookme.model.RecentlyViewed;
import com.example.bookme.service.RecentlyViewedService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/recently-viewed")
public class RecentlyViewedController {
    private final RecentlyViewedService recentlyViewedService;
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Page<RecentlyViewed> findAll(Authentication authentication,
                                        @PageableDefault(size = PageableConstants.PAGE_SIZE, page = PageableConstants.DEFAULT_PAGE) Pageable pageable){
        try{
            return recentlyViewedService.findAll(authentication, pageable);
        } catch (Exception e){
            return Page.empty();
        }
    }

    @PostMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> save(Authentication authentication,
                                  @PathVariable Long id){
        try{
            return recentlyViewedService.save(authentication, id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.badRequest().build());
        }catch (Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}
