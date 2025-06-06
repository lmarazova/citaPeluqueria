# CitaPeluqueriaApp

Aplicación web para la gestión de citas en una peluquería, diseñada para tres tipos de usuarios: **clientes**, **peluqueros** y **administradores**. La plataforma permite reservar, gestionar y cancelar citas de forma intuitiva y segura.

---

##  Tecnologías utilizadas

### Backend:
- Java 21
- Spring Boot
- Spring MVC
- Spring Security
- Maven
- MySQL

### Frontend:
- Thymeleaf (templating engine)
- HTML5
- CSS3
- JavaScript

### Otros:
- Bootstrap (diseño responsive)

---

##  Funcionalidades Principales

**CitaPeluqueriaApp** es una aplicación web pensada para facilitar la gestión de citas en una peluquería. Está diseñada para tres tipos de usuarios: clientes, peluqueros y administradores.

###  Gestión de usuarios
- Registro de nuevos usuarios (clientes), con confirmación vía correo electrónico.
- Inicio de sesión y recuperación de contraseña.
- Acceso diferenciado para:
   - **Clientes**
   - **Peluqueros** (registrados por el administrador)
   - **Administrador** (cuenta inicial por defecto: `admin@domain.com` / `admin123`)

###  Funciones del administrador

![Selección de horario por cliente](./screenshots/screenshot_4_admin_buttons.png)

- Crear servicios simples y combinarlos en packs personalizados.
- Definir duración del servicio en segmentos de 15 minutos.
  ![Selección de horario por cliente](./screenshots/screenshot_10_admin_service_creation.png)

- Clasificar los segmentos como:
   - **Activos** (requieren intervención del peluquero)
   - **Pasivos** (el cliente está esperando algún efecto)
- Asignar imágenes, precios y disponibilidad.
- Registrar peluqueros y clientes (por ejemplo, por llamada telefónica).
  ![Registrar peluquiero](./screenshots/screenshot_6_admin_hairdresser_register.png)
  ![Registrar clente](./screenshots/screenshot_7_admin_client_register.png)

- Asignar citas a clientes, con selección de fecha y franja horaria disponible.
- Consultar horarios disponibles por día.
- Añadir observaciones a los clientes.
- Cancelar citas.
  ![Añadir observaciones y cancelar citas](./screenshots/screenshot_12_add_dates_and_delete.png)
- 
- Ver todos los servicios disponibles, editarlos o desactivarlos.
- Modificar precios sin afectar citas existentes.
  ![Ver servicios disponbles, editar y desactivarlos](./screenshots/screenshot_9_admin_service_activation.png)


- Establecer días festivos para bloquear disponibilidad.
  ![Añadir dias festivos](./screenshots/screenshot_11_admin_holiday_creation.png)


###  Funciones del peluquero
- Acceder con su cuenta asignada.
- Gestionar las citas de sus propios clientes.
- Ver horarios disponibles, citas agendadas y detalles de cada servicio.
- No puede crear servicios nuevos ni modificar servicios y precios.

###  Funciones del cliente
- Registrarse y gestionar su perfil.

- Consultar todos los servicios y sus precios.
  ![Selección de servicio por cliente](./screenshots/screenshot_1_home_date.png)

- Seleccionar una fecha y ver franjas horarias disponibles.
  ![Selección de horario por cliente](./screenshots/screenshot_2_choose_service.png)

- Reservar una cita y visualizarla en su panel.
 ![Reservar cita y visualizarla en panel](./screenshots/screenshot_3_show_appointment.png)

- Cancelar citas si es necesario.


##  Puesta en marcha local (modo desarrollador)

1. **Requisitos previos:**
    - Java 21 instalado
    - MySQL (con base de datos configurada)
    - IntelliJ IDEA o tu IDE favorito
    - Maven instalado (opcional si usas IntelliJ)

2. **Pasos:**
    - Clona este repositorio:
      ```bash
      git clone https://github.com/lmarazova/CitaPeluqueriaApp.git
      ```
    - Abre el proyecto con IntelliJ
    - Configura `application.properties` con tu conexión a MySQL
    - Ejecuta la clase principal: `CitaPeluqueriaAppApplication`
    - Abre el navegador: [http://localhost:8082](http://localhost:8082)

---

##  Usuarios de ejemplo

Puedes iniciar sesión con usuarios ficticios para probar la app:

- Cliente: `26inmo@gmail.com` / `123123`
- Peluquero: `dani@gmail.com` / `123123`
- Admin: `admin@domain.com` / `admin123`

*(Estos datos pueden cambiar según tu configuración actual.)*

---

##  Estructura del proyecto (resumen rápido)

- `config/`
 Configuraciones generales del sistema (seguridad, correo, beans...):

AdminUserCreator, AppConfig, CustomMailConfig, SecurityConfig

- `domain/`
 Modelos de datos y transferencia (DTOs y entidades):

   -- `dtos/`: DTOs usados en controladores y servicios (AppointmentDTO, ClientShowTableDTO, etc.)
    
   -- `entities/`: Clases que representan la base de datos (AppointmentEntity, ClientEntity, etc.)

- `enums/`
 Enumeraciones para roles, tipos de servicio y estados de slots:

HairService, Role, ServicePackageEnum, SlotStatus

- `init/`
 Carga automática de datos iniciales:

HairdresserInit, HairServiceInit, ServiceInit

- `mapper/`
 Conversores entre entidades y DTOs:

AppointmentMapper, ClientMapper, ServiceMapper

- `repositories/`
 Interfaces que gestionan el acceso a la base de datos:

AppointmentRepository, ClientRepository, etc.

- `scheduling/`
 Tareas programadas para mantenimiento:

SchedulerTasks

- `services/`
 Lógica de negocio principal del sistema:

AppointmentService, HairdresserService, SlotService, etc.

- `util/`
 Clases auxiliares para formato y utilidades:

Constants, HourFormatter, SlotConverter, SlotProcessor

- `webController/`
 Controladores web que manejan las peticiones y vistas:

ActivationController, AppointmentController, AdminController, etc.

- `templates/`: archivos Thymeleaf (.html) : add-holiday.html, admin.html, login.html, register.html, service-pack.html, etc.
- `static/`: CSS, JS, imágenes

---

## Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más información.

---

