package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public Member findById(int id) {
        Member member = em.find(Member.class, id);
        return member;
    }

    public void save(Member member) {
        em.persist(member);
    }

    public Member findByLoginId(String loginId) {
        try {
            return em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                    .setParameter("loginId", loginId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Member findLogin(String loginId, String password) {
        try {
            return em.createQuery("select m from Member m where m.loginId = :loginId and m.password = :password ", Member.class)
                    .setParameter("loginId", loginId)
                    .setParameter("password", password)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Member findByPhone(String phone) {
        try {
            return em.createQuery("select m from Member m where m.phone = :phone ", Member.class)
                    .setParameter("phone", phone)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}