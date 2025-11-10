package flights_management.aeroops.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "aircraft")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Aircraft {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private String registration;

    @Column(nullable = false, length = 64)
    private String type;

    @Column(nullable = false, length = 64)
    private String manufacturer;

    @Column(nullable = false)
    private Integer seats;
}