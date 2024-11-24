package matal.global;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Server Controller", description = "서버 작동 여부 확인")
public class ServerController {

    @GetMapping("/api/health")
    @Operation(summary = "서버 동작 확인", description = "서버 동작을 확인하기 위해 사용하는 API")
    public String healthCheck() {
        return "hello, matal!";
    }
}
