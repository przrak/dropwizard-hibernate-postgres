package com.udemy;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class BookMarks3Configuration extends Configuration {
    // TODO: implement service configuration
    @NotEmpty
    private String password;

    @NotNull
    @Valid
    private DataSourceFactory dataSourceFactory
            = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }
}
