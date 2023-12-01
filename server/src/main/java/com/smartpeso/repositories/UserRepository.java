package com.smartpeso.repositories;

import com.smartpeso.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.bson.Document;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final String USER_COLLECTION = "users";
    private final MongoTemplate mongoTemplate;

    public UserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public User createUser(String email, String firstName, String lastName) {
        checkIfUserExistsByEmail(email);
        try {
            Document userDocument = createUserDocument(email, firstName, lastName);
            mongoTemplate.insert(userDocument, USER_COLLECTION);
            return new User(userDocument.getString("_id"), email, firstName, lastName);
        } catch(Exception e) {
            throw new UserCreationException("Failed creating new user, got error: " + e.getMessage());
        }
    }

    public Optional<User> findByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        User user = mongoTemplate.findOne(query, User.class, USER_COLLECTION);
        return Optional.ofNullable(user);
    }

    private void checkIfUserExistsByEmail(String email) {
        if (findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
        }
    }

    private Document createUserDocument(String email, String firstName, String lastName) {
        String userId = UUID.randomUUID().toString();
        return new Document()
                .append("_id", userId)
                .append("email", email)
                .append("firstName", firstName)
                .append("lastName", lastName);
    }
}
