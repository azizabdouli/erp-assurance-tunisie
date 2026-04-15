package com.erp.assurance.tunisie.reporting.controller;

import com.erp.assurance.tunisie.reporting.dto.DashboardKpi;
import com.erp.assurance.tunisie.reporting.service.DashboardService;
import com.erp.assurance.tunisie.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard and KPI endpoints")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/kpis")
    @Operation(summary = "Get dashboard KPIs")
    public ResponseEntity<ApiResponse<DashboardKpi>> getDashboardKpis() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getDashboardKpis()));
    }
}
