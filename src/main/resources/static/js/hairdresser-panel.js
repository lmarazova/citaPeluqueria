   const csrfToken = document.querySelector('input[name="_csrf"]').value;

    /* ------------ CARGA LISTA DE CLIENTES AL INICIAR ------------ */
    document.addEventListener('DOMContentLoaded', () => {
      console.log('DOM cargado ✅');

      fetch('/api/clients')
        .then(res => res.json())
        .then(clients => {
          console.log('Clientes recibidos:', clients);
          const select = document.getElementById('client-select');

          clients.forEach(client => {
            const option = document.createElement('option');
            option.value = client.id;
            option.textContent = client.username;
            select.appendChild(option);
          });
        })
        .catch(err => {
          console.error('⚠️ Error al cargar los clientes:', err);
        });
    });
    /* ----------- FIN CARGA LISTA DE CLIENTES AL INICIAR --------- */


    /* ---------------------- DETALLE CLIENTE --------------------- */
    function loadClientDetails(clientId) {
      if (!clientId) return; // nada seleccionado

      fetch('/api/clients/' + clientId)
        .then(res => res.json())
        .then(client => {
          console.log('Detalles del cliente:', client);

          // Datos básicos
          document.getElementById('client-name').textContent     = client.username;
          document.getElementById('client-email').textContent    = client.email    || 'n/a';
          document.getElementById('client-phone').textContent    = client.phone    || 'n/a';
          document.getElementById('client-role').textContent     = client.role     || 'n/a';
          document.getElementById('client-comments').textContent = client.comments || '';
          document.getElementById('client-guest').textContent    = client.guest ? 'Sí' : 'No';
          document.getElementById('client-notes').value          = client.comments || '';

          // Mostrar tarjeta
          //document.getElementById('client-card').style.display = 'block';
          const clientModal = new bootstrap.Modal(document.getElementById('clientModal'));
          clientModal.show();

          // Citas
          const list = document.getElementById('client-appointments');
          list.innerHTML = ''; // limpiar

          if (client.appointmentDTOS && client.appointmentDTOS.length) {
            client.appointmentDTOS.forEach((appt, idx) => {
  const li = document.createElement('li');
  li.innerHTML = `
    <strong>${idx + 1}.</strong>
    Fecha: ${appt.date},
    Horario: ${appt.selectedHourRange},
    Servicio: ${appt.service?.description || ''}
    <button class="btn btn-sm btn-danger ms-2"
            onclick="deleteAppointment(${client.id}, '${appt.date}', '${appt.selectedHourRange}')">
      Eliminar
    </button>
  `;
  list.appendChild(li);
});

          } else {
            list.innerHTML = '<li>Sin citas registradas</li>';
          }
        })
        .catch(err => console.error('Error al cargar cliente:', err));
    }
    /* ------------------- FIN DETALLE CLIENTE -------------------- */


function saveNotes() {
  const userId = document.getElementById('client-select').value;
  const comments = document.getElementById('client-notes').value;

  fetch('/api/clients/moderator/update-comments', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'X-CSRF-TOKEN': csrfToken
    },
    body: new URLSearchParams({ userId, comments })
  })
  .then(res => {
    if (!res.ok) throw new Error('Error al guardar las observaciones.');
    return res.text();
  })
  .then(msg => {
    alert('✅ Observaciones guardadas');
    // 👉 Actualiza el campo visible también
    document.getElementById('client-comments').textContent = comments;
  })
  .catch(err => console.error(err));
}




    /* ------------------------------------------------------------ */
function deleteAppointment(userId, date, selectedHourRange) {
  fetch('/api/clients/moderator/delete-appointment', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'X-CSRF-TOKEN': csrfToken
    },
    body: new URLSearchParams({ userId, date, selectedHourRange })
  })
  .then(res => {
    if (!res.ok) throw new Error('Error al eliminar la cita.');
    return res.text();
  })
  .then(msg => {
    alert('✅ Cita eliminada');
    // 👉 Recarga los datos del cliente para actualizar la lista de citas
    loadClientDetails(userId);
  })
  .catch(err => console.error(err));
}

