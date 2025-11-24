package flights_management.aeroops.entity;

//Passenger(id, first_name, last_name, email, phone)

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Passenger {
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
