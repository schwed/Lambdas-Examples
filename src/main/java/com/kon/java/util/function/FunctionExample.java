package com.kon.java.util.function;

import java.util.function.Function;
import java.util.logging.Logger;

import com.google.common.base.Objects;


public class FunctionExample {
    private static final Logger logger = Logger.getLogger(FunctionExample.class.getName());

    static class Person {

        private int personId;
        private String jobDescription;

        public Person(int personId, String jobDescription) {
            super();
            this.personId = personId;
            this.jobDescription = jobDescription;
        }

        public int getPersonId() {
            return personId;
        }

        public String getJobDescription() {
            return jobDescription;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(Person.class)
                    .add("personid", personId)
                    .add("job description", jobDescription).toString();
        }

    }

    static class Job {

        private int personId;
        private String description;

        public Job(int personId, String description) {
            super();
            this.personId = personId;
            this.description = description;
        }

        public int getPersonId() {
            return personId;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(Job.class).add("personId", personId)
                    .add("job description", description).toString();
        }

    }

    // functional interface
    /*
    Function<Person, Job> mapPersonToJob = new Function<Person, Job>() {
        public Job apply(Person person) {
            Job job = new Job(person.getPersonId(), person.getJobDescription());
            return job;
        }
    };
    */

    // functional interface using lambda
    public static Function<Person, Job> mapPersonToJob = person -> {
        Job job = new Job(person.getPersonId(), person.getJobDescription());
        return job;
    };


}
