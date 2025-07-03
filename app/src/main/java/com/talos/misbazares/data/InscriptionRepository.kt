package com.talos.misbazares.data

import com.talos.misbazares.data.db.InscriptionDao
import com.talos.misbazares.data.db.model.InscriptionEntity

class InscriptionRepository(private val inscriptionDao: InscriptionDao) {

    suspend fun solicitarInscripcion(eventId: Int, sellerId: Long) {
        inscriptionDao.insertInscription(
            InscriptionEntity(eventId, sellerId, "solicitado")
        )
    }

    suspend fun aprobarInscripcion(eventId: Int, sellerId: Long) {
        inscriptionDao.updateStatus(eventId, sellerId, "aceptado")
    }

    suspend fun rechazarInscripcion(eventId: Int, sellerId: Long) {
        inscriptionDao.updateStatus(eventId, sellerId, "rechazado")
    }

    suspend fun cancelarInscripcion(eventId: Int, sellerId: Long) {
        inscriptionDao.deleteInscription(
            InscriptionEntity(eventId, sellerId, "")
        )
    }

    suspend fun obtenerInscripcionesVendedor(sellerId: Long): List<InscriptionEntity> {
        return inscriptionDao.getInscriptionsForSeller(sellerId)
    }

    suspend fun obtenerInscripcionesEvento(eventId: Int): List<InscriptionEntity> {
        return inscriptionDao.getInscriptionsForEvent(eventId)
    }
}
