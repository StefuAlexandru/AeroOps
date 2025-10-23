package flights_management.aeroops.web;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestDbController {

    private final JdbcTemplate jdbc;

    public TestDbController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @PostConstruct
    public void ensureTable() {
        jdbc.execute("""
            CREATE TABLE IF NOT EXISTS ping(
              id SERIAL PRIMARY KEY,
              note TEXT,
              created_at TIMESTAMPTZ DEFAULT now()
            )
        """);
    }

    @PostMapping("/ping")
    public Map<String, Object> ping(@RequestParam(defaultValue = "ok") String note) {
        int inserted = jdbc.update("INSERT INTO ping(note) VALUES (?)", note);
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM ping", Integer.class);

        Map<String, Object> resp = new HashMap<>();
        resp.put("inserted", inserted);
        resp.put("count", count);
        return resp;
    }

    @GetMapping("/ping/count")
    public Map<String, Integer> count() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM ping", Integer.class);
        return Map.of("count", count);
    }
}
