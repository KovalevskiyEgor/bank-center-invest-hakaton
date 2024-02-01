package com.bank.repositories;

import com.bank.models.Post;
import com.bank.models.User;
import com.bank.utils.enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByTitle(String title);
    List<Post> findAllByTitle(String title);
    List<Post> findAllByType(PostType type);
    boolean existsPostByUser(User user);
}
