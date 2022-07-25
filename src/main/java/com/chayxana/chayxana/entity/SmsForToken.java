package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.template.AbsEntityLong;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SmsForToken extends AbsEntityLong {

    @NotNull(message = "email bo`sh bo`lishi mumkin emas")
    private String email="test@eskiz.uz";

    @NotNull(message = "parol bo`sh bo`lishi mumkin emas")
    private String password="j6DWtQjjpLDNjWEk74Sx";

    @Type(type = "text")
    private String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjUsInJvbGUiOiJ1c2VyIiwiZGF0YSI6eyJpZCI6NSwibmFtZSI6Ilx1MDQyN1x1MDQxZiBCZXN0IEludGVybmV0IFNvbHV0aW9uIiwiZW1haWwiOiJ0ZXN0QGVza2l6LnV6Iiwicm9sZSI6InVzZXIiLCJhcGlfdG9rZW4iOm51bGwsInN0YXR1cyI6ImFjdGl2ZSIsInNtc19hcGlfbG9naW4iOiJlc2tpejIiLCJzbXNfYXBpX3Bhc3N3b3JkIjoiZSQkayF6IiwidXpfcHJpY2UiOjUwLCJiYWxhbmNlIjo3NTUwLCJpc192aXAiOjAsImhvc3QiOiJzZXJ2ZXIxIiwiY3JlYXRlZF9hdCI6bnVsbCwidXBkYXRlZF9hdCI6IjIwMjItMDQtMTlUMTM6MzY6NTEuMDAwMDAwWiJ9LCJpYXQiOjE2NTA0OTkwNDcsImV4cCI6MTY1MzA5MTA0N30.dcANlfii2_LaS5eA8jGB2LFDjOMg-eSjLhZnr0WzsQw";

    private String url="https://notify.eskiz.uz/api/auth/login";



}
