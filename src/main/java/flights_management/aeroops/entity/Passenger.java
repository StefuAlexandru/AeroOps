package flights_management.aeroops.entity;

//Passenger(id, first_name, last_name, email, phone)

import flights_management.aeroops.util.TimeStamps;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Passenger extends TimeStamps {
          @Id
          @GeneratedValue(strategy = GenerationType.IDENTITY)
          private Long id;

          @Column(nullable = false)
          private String firstName;

          @Column(nullable = false)
          private String lastName;

          @Column(nullable = false,unique = true)
          private String email;

          @Column(nullable = false,unique = true)
          private String phone;

}
