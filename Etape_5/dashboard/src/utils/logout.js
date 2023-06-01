export function logout() {
    document.cookie = "token=; expires=Thu, 01 Jan 1900 00:00:00 UTC;";
    return window.location = "http://localhost:3000";
}