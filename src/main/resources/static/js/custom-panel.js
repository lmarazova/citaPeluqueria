
    document.getElementById("phone").addEventListener("blur", function () {
        const phone = this.value.trim();
        const usernameField = document.getElementById("username");

        if (phone !== "") {
            fetch(`/moderator/check-client?phone=${encodeURIComponent(phone)}`)
                .then(response => {
                    if (response.ok) return response.json();
                    throw new Error("Cliente no encontrado");
                })
                .then(data => {
                    usernameField.value = data.username;
                    usernameField.disabled = true;
                    showClientMessage("Cliente ya registrado ✅", "success");
                })
                .catch(() => {
                    usernameField.value = "";
                    usernameField.disabled = false;
                    showClientMessage("Nuevo cliente a registrar", "info");
                });
        }
    });

    function showClientMessage(message, type) {
        const container = document.getElementById("clientMessage");
        container.textContent = message;
        container.className = `alert alert-${type}`;
        container.style.display = "block";
    }

    function submitToRefreshHours() {
        const form = document.getElementById('appointmentForm');

        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }

        const phone = document.getElementById('phone').value;
        const username = document.getElementById('username').value;
        const date = document.getElementById('date').value;
        const packageId = document.getElementById('packageId').value;

        if (!phone || !username || !date || !packageId) {
            alert("Por favor, rellena todos los campos antes de continuar");
            return;
        }

        const url = `/moderator/refresh-hours?phone=${encodeURIComponent(phone)}&username=${encodeURIComponent(username)}&date=${encodeURIComponent(date)}&packageId=${encodeURIComponent(packageId)}`;

        window.location.href = url;
    }


      function closeAccordion(id) {
        const element = document.getElementById(id);
        const collapseInstance = bootstrap.Collapse.getInstance(element);
        if (collapseInstance) {
          collapseInstance.hide();
        } else {
          const newCollapse = new bootstrap.Collapse(element);
          newCollapse.hide();
        }
      }
    function openClientModal(clientId) {
      if (!clientId) return;
      loadClientDetails(clientId);
    }
  const myModal = document.getElementById('myModal')
          const myInput = document.getElementById('myInput')
          myModal.addEventListener('shown.bs.modal', () => {
            myInput.focus()
          })



