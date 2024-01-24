// package com.example.watch_selling.repository;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

// import com.example.watch_selling.model.User;
// import java.util.List;
// import java.util.Optional;


// public interface UserRepository extends JpaRepository<User, Long>{
//     @Query("Create (u:User {email: :email, password: :password})")
//     public void saveAccount(
//         @Param("email") String email,
//         @Param("password") String password    
//     );

//     // @Override
//     // public List<User> findAll() {
//     //     // TODO Auto-generated method stub
//     //     throw new UnsupportedOperationException("Unimplemented method 'findAll'");
//     // }

//     // @Override
//     // public Optional<User> findById(Long id) {
//     //     // TODO Auto-generated method stub
//     //     throw new UnsupportedOperationException("Unimplemented method 'findById'");
//     // }
// }