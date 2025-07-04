package com.talos.misbazares.data

import com.talos.misbazares.data.db.InscriptionDao
import com.talos.misbazares.data.db.model.InscriptionEntity
class InscriptionRepository(
    private val inscriptionDao: InscriptionDao
) {
    suspend fun insertInscription(inscription: InscriptionEntity) {
        inscriptionDao.insertInscription(inscription)
    }

    suspend fun updateInscriptionStatus(eventId: Int, sellerId: Long, newStatus: String) {
        inscriptionDao.updateStatus(eventId, sellerId, newStatus)
    }

    suspend fun deleteInscription(inscription: InscriptionEntity) {
        inscriptionDao.deleteInscription(inscription)
    }

    suspend fun getInscriptionsForSeller(sellerId: Long): List<InscriptionEntity> {
        return inscriptionDao.getInscriptionsForSeller(sellerId)
    }

    suspend fun getInscriptionsForEvent(eventId: Int): List<InscriptionEntity> {
        return inscriptionDao.getInscriptionsForEvent(eventId)
    }

    suspend fun getInscription(eventId: Int, sellerId: Long): InscriptionEntity? {
        return inscriptionDao.getInscription(eventId, sellerId)
    }
}
