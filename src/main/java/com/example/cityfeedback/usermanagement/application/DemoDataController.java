package com.example.cityfeedback.usermanagement.application;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/demo-data")
public class DemoDataController {

    private final DemoDataService demoDataService;

    public DemoDataController(DemoDataService demoDataService) {
        this.demoDataService = demoDataService;
    }

    /**
     * Löscht alle Demo-Daten.
     * DELETE /admin/demo-data
     */
    @DeleteMapping
    public DemoDataDeleteResponse deleteAllDemoData(
            @RequestHeader(value = "X-Admin-Id", required = true) UUID adminId) {
        int deletedCount = demoDataService.deleteAllDemoData(adminId);
        return new DemoDataDeleteResponse(deletedCount, "Demo-Daten erfolgreich gelöscht");
    }

    public static class DemoDataDeleteResponse {
        public final int deletedUsers;
        public final String message;

        public DemoDataDeleteResponse(int deletedUsers, String message) {
            this.deletedUsers = deletedUsers;
            this.message = message;
        }
    }
}

