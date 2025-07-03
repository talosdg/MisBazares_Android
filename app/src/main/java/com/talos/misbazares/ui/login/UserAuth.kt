package com.talos.misbazares.ui.login

import com.talos.misbazares.data.db.model.UsersEntity

object UserAuth {

    // Mapa de usuario a (password, rol)
    private val usuarios = mapOf(
        "juana" to ("clave123" to 0),
        "ana" to ("clave123" to 0),
        "esther" to ("clave123" to 0),
        "herlinda" to ("clave123" to 0),
        "eloisa" to ("clave123" to 0),
        "willi" to ("clave123" to 0),
        "martin" to ("admin123" to 2),
        "gina" to ("admin123" to 2)
    )

    fun autenticar(usuario: String, password: String): Pair<Boolean, Int?> {
        val datos = usuarios[usuario]
        return if (datos != null && datos.first == password) {
            true to datos.second
        } else {
            false to null
        }
    }

    /**
     * Lista de todos los usuarios (para simular la lista de sellers)
     */
    fun getAllSellers(): List<UsersEntity> {
        return usuarios
            .filter { it.value.second == 0 }  // solo rol vendedor
            .map { (nombre, datos) ->
                UsersEntity(name = nombre, secondname = "",  email = "", rol = datos.second, events = "", password = "" )
            }
    }

    /*** Lista de todos los admins

    fun getAllAdmins(): List<UsersEntity> {
        return usuarios
            .filter { it.value.second == 2 }  // solo admins
            .map { (nombre, datos) ->
                UsersEntity(name = nombre, rol = datos.second)
            }
    }

*/
}
