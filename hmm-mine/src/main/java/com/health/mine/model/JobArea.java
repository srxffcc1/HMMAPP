package com.health.mine.model;

import java.io.Serializable;

public class JobArea implements Serializable {

        public String name;
        public String value;

        public JobArea(String name, String value) {
                this.name = name;
                this.value = value;
        }
}
