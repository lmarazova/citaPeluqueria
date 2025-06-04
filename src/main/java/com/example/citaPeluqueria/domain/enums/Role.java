package com.example.citaPeluqueria.domain.enums;
/**
 * Enum que representa los distintos roles que puede tener un usuario en el sistema.
 *
 * <ul>
 *   <li>{@code USER} – Cliente registrado con acceso estándar.</li>
 *   <li>{@code ADMIN} – Administrador con acceso completo al sistema.</li>
 *   <li>{@code MODERATOR} – Moderador con permisos de gestión limitados (como gestión de citas).</li>
 *   <li>{@code GUEST} – Usuario provisional creado por un moderador cuando un cliente aún no se ha registrado por su cuenta.</li>
 * </ul>
 *
 * El método {@link #toString()} sobrescribe el valor para que cada rol se represente con el prefijo {@code "ROLE_"}.
 */
public enum Role {
    USER,
    ADMIN,
    MODERATOR,
    GUEST;

    /**
     * Devuelve una representación en texto del rol, con el prefijo {@code ROLE_}.
     *
     * @return el nombre del rol en formato {@code ROLE_NOMBRE}.
     */
    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
