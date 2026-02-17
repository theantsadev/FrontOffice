/**
 * ===== GESTION CENTRALISÉE DES RÉPONSES API =====
 * Helper pour standardiser tous les appels API
 * Gère les tokens, les erreurs et les réponses
 */

/**
 * Traite la réponse API de façon cohérente
 * @param {Object} response - La réponse JSON de l'API
 * @param {Function} onSuccess - Callback en cas de succès
 * @param {Function} onError - Callback en cas d'erreur
 * @returns {boolean} true si succès, false sinon
 */
function handleApiResponse(response, onSuccess, onError) {
    // Déterminer le statut
    const isSuccess = response.status === 'success' || (response.code >= 200 && response.code < 300);
    const isError = response.status === 'error' || response.code >= 400;
    
    console.log('API Response:', response);
    
    if (isError) {
        // Extraire le message d'erreur
        const errorMessage = response.message || 'Erreur inconnue';
        const errorCode = response.code || 500;
        
        console.error('API Error:', errorCode, errorMessage);
        
        if (onError) {
            onError(errorCode, errorMessage);
        } else {
            showApiError(errorCode, errorMessage);
        }
        return false;
    }
    
    if (isSuccess) {
        if (onSuccess) {
            onSuccess(response.data || response);
        }
        return true;
    }
    
    return false;
}

/**
 * ===== AFFICHAGE CENTRALISÉ DES ERREURS =====
 */
function showApiError(code, message) {
    const errorDiv = document.getElementById('loadingDiv');
    if (errorDiv) {
        let icon = '❌';
        let title = 'Erreur';
        
        if (code === 403) {
            icon = '🔐';
            title = 'Accès refusé';
        } else if (code === 404) {
            icon = '🔍';
            title = 'Non trouvé';
        } else if (code === 500) {
            icon = '⚠️';
            title = 'Erreur serveur';
        }
        
        errorDiv.innerHTML = '' +
            '<div class="alert alert-danger alert-dismissible fade show" role="alert">' +
                '<strong>' + icon + ' ' + title + ' (' + code + ')</strong><br>' +
                message +
                '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
            '</div>';
    }
}

/**
 * ===== GESTION DU TOKEN =====
 */

/**
 * Récupère le token stocké localement
 */
function getStoredToken() {
    return localStorage.getItem('api_token');
}

/**
 * Stocke le token localement
 */
function setStoredToken(token) {
    localStorage.setItem('api_token', token);
}

/**
 * Supprime le token stocké
 */
function clearStoredToken() {
    localStorage.removeItem('api_token');
}

/**
 * Demande un token à l'utilisateur
 */
function promptForToken() {
    const token = prompt('Veuillez entrer le token d\'authentification:');
    if (token && token.trim()) {
        setStoredToken(token.trim());
        return token.trim();
    }
    return null;
}

/**
 * ===== FETCH AVEC GESTION DU TOKEN =====
 */

/**
 * Effectue un appel API avec gestion automatique du token
 * @param {string} url - L'URL de l'API
 * @param {Object} options - Options fetch (method, body, etc.)
 * @param {boolean} requireToken - Si true, un token est requis
 * @returns {Promise<Object>} La réponse JSON
 */
async function fetchApi(url, options = {}, requireToken = true) {
    let token = getStoredToken();
    console.log('Token actuel:', token ? '***' : 'vide');
    
    if (requireToken && !token) {
        token = promptForToken();
        if (!token) {
            throw new Error('Token requis pour accéder aux données');
        }
    }
    
    // Déterminer la méthode HTTP
    const method = options.method || 'GET';
    
    // Construire l'URL avec le token
    let finalUrl = url;
    
    if (token) {
        const separator = url.includes('?') ? '&' : '?';
        finalUrl = url + separator + 'token=' + encodeURIComponent(token);
    }
    
    console.log('Calling API:', method, finalUrl);
    
    const response = await fetch(finalUrl, options);
    const text = await response.text();
    
    // Gérer les réponses vides
    if (!text || text.trim() === '') {
        return { status: 'success', code: response.status };
    }
    
    return JSON.parse(text);
}

/**
 * ===== FONCTION UTILITAIRE: ESCAPE HTML =====
 * Prévient les attaques XSS
 */
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return String(text).replace(/[&<>"']/g, m => map[m]);
}

/**
 * ===== FONCTION UTILITAIRE: ENCODE FORM DATA =====
 */
function encodeFormData(formData) {
    if (formData instanceof FormData) {
        return new URLSearchParams(formData);
    }
    if (typeof formData === 'object') {
        return new URLSearchParams(formData);
    }
    return formData;
}

/**
 * ===== FORMAT DE DATE =====
 */
function formatDateFR(dateStr) {
    const date = new Date(dateStr);
    return date.toLocaleString('fr-FR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}
