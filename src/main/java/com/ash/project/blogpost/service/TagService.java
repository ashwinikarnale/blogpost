package com.ash.project.blogpost.service;

import com.ash.project.blogpost.model.Tag;
import com.ash.project.blogpost.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagService {

    @Autowired
    TagRepository tagRepository;

    public List<String> findAllTags() {
        Set<String> tagName = new HashSet<>();
        for(Tag tag:tagRepository.findAll()) {
            tagName.add(tag.getName());
        }
        return new ArrayList<>(tagName);
    }
    public String findTagByPostId(int id) {
        List<String> tags = tagRepository.findTagByPostId(id);
        StringBuilder mutableString = new StringBuilder();
        for (String tag : tags) {
            mutableString.append(tag + ",");
        }
        mutableString.delete(mutableString.length() - 1,mutableString.length());
        return mutableString.toString();
    }

}
