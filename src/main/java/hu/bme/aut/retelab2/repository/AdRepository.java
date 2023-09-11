package hu.bme.aut.retelab2.repository;

import hu.bme.aut.retelab2.domain.Ad;
import hu.bme.aut.retelab2.secret_generator.SecretGenerator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AdRepository
{
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Ad save(Ad ad)
    {
        ad.setCode(SecretGenerator.generate());
        return em.merge(ad);
    }

    public List<Ad> searchPrice(int minPrice, int maxPrice)
    {
        List<Ad> result = em.createQuery("select x from Ad x where x.price between ?1 and ?2", Ad.class)
                .setParameter(1, minPrice).setParameter(2, maxPrice).getResultList();
        for(Ad ad: result)
            ad.setCode(null);
        return result;
    }

    @Transactional
    public Ad update (Ad updated)
    {
        Ad checking = em.find(Ad.class, updated.getId());
        if(!checking.getCode().equals(updated.getCode()))
            return null;
        save(updated);
        return updated;
    }
}
