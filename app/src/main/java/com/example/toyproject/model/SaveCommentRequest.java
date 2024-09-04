package com.example.toyproject.model;

public class SaveCommentRequest {
    Long postId;
    String comment;

    public SaveCommentRequest(Long postId, String comment) {
        this.postId = postId;
        this.comment = comment;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
