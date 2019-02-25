package com.learn.simplerestapp.dto;

public class Views {

    public interface Public {
    }

    public interface Full extends Public {
    }

    public interface Admin extends Full {
    }
}
