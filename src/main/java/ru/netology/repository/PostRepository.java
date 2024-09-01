package ru.netology.repository;

import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {

    private static final AtomicLong postsNum = new AtomicLong(0);
    private final List<Post> posts = new CopyOnWriteArrayList<>();

    public List<Post> all() {
        return posts;
    }

    public Optional<Post> getById(long id) {
        if (postsNum.get() == 0) return Optional.empty();
        return posts.stream().filter(p -> p.getId() == id).findFirst();
    }

    public Post save(Post post) {
        if (post.getId() == 0 || post.getId() > posts.size()) {
            post.setId(postsNum.incrementAndGet());
            posts.add(post);
        } else {
            posts.set((int) post.getId() - 1, post);
        }
        return post;
    }

    public void removeById(long id) {
        posts.removeIf(post -> post.getId() == id);
    }
}
