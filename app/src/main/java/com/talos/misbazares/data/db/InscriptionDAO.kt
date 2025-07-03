package com.talos.misbazares.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.talos.misbazares.data.db.model.InscriptionEntity
import com.talos.misbazares.util.Constants
@Dao
interface InscriptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInscription(inscription: InscriptionEntity)

    @Query("""
        UPDATE ${Constants.DATABASE_INSCRIPTIONS_TABLE} 
        SET status = :newStatus 
        WHERE event_id = :eventId AND seller_id = :sellerId
    """)
    suspend fun updateStatus(eventId: Int, sellerId: Long, newStatus: String)

    @Delete
    suspend fun deleteInscription(inscription: InscriptionEntity)

    @Query("""
        SELECT * FROM ${Constants.DATABASE_INSCRIPTIONS_TABLE}
        WHERE seller_id = :sellerId
    """)
    suspend fun getInscriptionsForSeller(sellerId: Long): List<InscriptionEntity>

    @Query("""
        SELECT * FROM ${Constants.DATABASE_INSCRIPTIONS_TABLE}
        WHERE event_id = :eventId
    """)
    suspend fun getInscriptionsForEvent(eventId: Int): List<InscriptionEntity>

    @Query("""
        SELECT * FROM ${Constants.DATABASE_INSCRIPTIONS_TABLE}
        WHERE event_id = :eventId AND seller_id = :sellerId
        LIMIT 1
    """)
    suspend fun getInscription(eventId: Int, sellerId: Long): InscriptionEntity?
}
