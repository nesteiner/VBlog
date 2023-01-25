package com.example.backend.generator

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentityGenerator
import java.io.Serializable

class ManualInsertGenerator: IdentityGenerator() {
    override fun generate(s: SharedSessionContractImplementor, obj: Any): Serializable {
        val id = s.getEntityPersister(null, obj).classMetadata.getIdentifier(obj, s) as Serializable?
        if(id != null && Integer.valueOf(id.toString()) > 0) {
            return id
        } else {
            return super.generate(s, obj) as Serializable
        }
    }
}