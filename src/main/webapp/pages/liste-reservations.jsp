<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Réservations</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #1a5276 0%, #0b3d5e 100%);
            min-height: 100vh;
            padding: 40px 20px;
        }
        .container-custom {
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.1);
            padding: 40px;
            max-width: 1200px;
            margin: 0 auto;
        }
        .page-title {
            color: #1a5276;
            margin-bottom: 30px;
            font-weight: 700;
        }
        .filter-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 30px;
        }
        .table-custom {
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        .table thead {
            background: #1a5276;
            color: white;
        }
        .badge-custom {
            padding: 8px 12px;
            font-weight: 500;
        }
        .loading {
            text-align: center;
            padding: 40px;
            color: #1a5276;
        }
    </style>
</head>
<body>
    <div class="container-custom">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="page-title mb-0">Liste des Réservations</h2>
            <div>
                <a href="${pageContext.request.contextPath}/pages/" class="btn btn-outline-secondary">Accueil</a>
            </div>
        </div>

        <div class="filter-section">
            <div class="row align-items-end">
                <div class="col-md-4">
                    <label for="filterDate" class="form-label fw-bold">Rechercher par date</label>
                    <input type="date" class="form-control" id="filterDate">
                </div>
                <div class="col-md-2">
                    <button class="btn btn-primary w-100" onclick="filterByDate()">Rechercher</button>
                </div>
                <div class="col-md-2">
                    <button class="btn btn-outline-secondary w-100" onclick="showAll()">Afficher tout</button>
                </div>
            </div>
        </div>

        <div id="loadingDiv" class="loading">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Chargement...</span>
            </div>
            <p class="mt-2">Chargement des réservations...</p>
        </div>

        <div id="errorDiv" style="display: none;">
            <div class="alert alert-danger" id="errorMessage"></div>
        </div>

        <div id="tableContainer" style="display: none;">
            <div class="table-responsive">
                <table class="table table-hover table-custom">
                    <thead>
                        <tr>
                            <th>ID Réservation</th>
                            <th>ID Client</th>
                            <th>Hôtel</th>
                            <th>Nombre de passagers</th>
                            <th>Date et heure d'arrivée</th>
                        </tr>
                    </thead>
                    <tbody id="reservationTableBody">
                    </tbody>
                </table>
            </div>
            <div id="emptyMessage" class="text-center text-muted py-5" style="display: none;">
                <h5>Aucune réservation trouvée</h5>
                <p>Aucune réservation ne correspond à votre recherche</p>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const contextPath = '${pageContext.request.contextPath}';

        async function loadReservations(url) {
            if (!url) {
                url = contextPath + '/reservations';
            }

            document.getElementById('loadingDiv').style.display = 'block';
            document.getElementById('tableContainer').style.display = 'none';
            document.getElementById('errorDiv').style.display = 'none';

            try {
                const response = await fetch(url);
                const text = await response.text();
                console.log('Raw response:', text);

                const result = JSON.parse(text);
                console.log('Parsed result:', result);

                const tbody = document.getElementById('reservationTableBody');
                tbody.innerHTML = '';

                let reservations = [];
                if (result.status === 'success' && result.data) {
                    reservations = result.data;
                } else if (Array.isArray(result.data)) {
                    reservations = result.data;
                } else if (Array.isArray(result)) {
                    reservations = result;
                } else if (result.status === 'error') {
                    throw new Error(result.message || 'Erreur du serveur');
                }

                console.log('Reservations array:', reservations);

                if (reservations.length > 0) {
                    reservations.forEach(function(reservation, index) {
                        console.log('Reservation ' + index + ':', reservation);

                        var date = new Date(reservation.date_heure_arrivee);
                        var formattedDate = date.toLocaleString('fr-FR', {
                            year: 'numeric',
                            month: '2-digit',
                            day: '2-digit',
                            hour: '2-digit',
                            minute: '2-digit'
                        });

                        var row = document.createElement('tr');
                        row.innerHTML =
                            '<td><span class="badge bg-primary badge-custom">' + reservation.id_reservation + '</span></td>' +
                            '<td><strong>' + reservation.id_client + '</strong></td>' +
                            '<td>' + (reservation.nom_hotel || 'N/A') + '</td>' +
                            '<td>' + reservation.nb_passager + '</td>' +
                            '<td>' + formattedDate + '</td>';
                        tbody.appendChild(row);
                    });
                    document.getElementById('emptyMessage').style.display = 'none';
                } else {
                    document.getElementById('emptyMessage').style.display = 'block';
                }

                document.getElementById('loadingDiv').style.display = 'none';
                document.getElementById('tableContainer').style.display = 'block';
            } catch (error) {
                console.error('Erreur:', error);
                document.getElementById('loadingDiv').style.display = 'none';
                document.getElementById('errorDiv').style.display = 'block';
                document.getElementById('errorMessage').textContent =
                    'Erreur lors du chargement des réservations : ' + error.message;
            }
        }

        function filterByDate() {
            var dateInput = document.getElementById('filterDate');
            if (dateInput.value) {
                loadReservations(contextPath + '/reservations?date=' + dateInput.value);
            } else {
                alert('Veuillez sélectionner une date');
            }
        }

        function showAll() {
            document.getElementById('filterDate').value = '';
            loadReservations();
        }

        window.onload = function() {
            loadReservations();
        };
    </script>
</body>
</html>
