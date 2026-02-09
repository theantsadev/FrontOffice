<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FrontOffice - Réservations Hôtelières</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #1a5276 0%, #0b3d5e 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .main-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.1);
            padding: 40px;
            max-width: 600px;
            width: 100%;
        }
        .main-title {
            color: #1a5276;
            margin-bottom: 30px;
            font-weight: 700;
        }
        .menu-btn {
            margin: 10px 0;
            padding: 15px;
            font-size: 16px;
            font-weight: 500;
        }
    </style>
</head>
<body>
    <div class="main-card">
        <h1 class="main-title text-center">Réservations Hôtelières</h1>
        <p class="text-center text-muted mb-4">FrontOffice - Consultation des réservations</p>

        <div class="d-grid gap-3">
            <a href="${pageContext.request.contextPath}/pages/liste-reservations" class="btn btn-primary menu-btn">
                Voir les Réservations
            </a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
