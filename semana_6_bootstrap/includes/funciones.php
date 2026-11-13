<?php
declare(strict_types=1);

/**
 * includes/funciones.php
 * Funciones reutilizables. Se carga siempre con require_once para evitar
 * errores de "función redeclarada".
 *
 * Regla: los datos se NORMALIZAN al recibirlos (textoPost, trim) y se ESCAPAN
 * al mostrarlos (e). Nunca se guarda en la base texto ya escapado.
 */

// La sesión se inicia (con su configuración segura) en auth.php
require_once __DIR__ . "/auth.php";

/* ----------------------------------------------------------- escape y entrada */

/**
 * Escapa un valor para mostrarlo en HTML, dentro de texto o de atributos (solo para SALIDA).
 * ENT_QUOTES escapa comillas simples y dobles; ENT_SUBSTITUTE reemplaza bytes UTF-8 inválidos
 * (sin esa opción, un texto mal codificado devolvería una cadena vacía).
 * Para armar direcciones usar http_build_query() / rawurlencode(), no e().
 */
function e($valor): string
{
    return htmlspecialchars((string) $valor, ENT_QUOTES | ENT_SUBSTITUTE, "UTF-8");
}

/** true si el texto es UTF-8 válido y no contiene caracteres de control. */
function textoSeguro(string $texto): bool
{
    return mb_check_encoding($texto, "UTF-8") && !preg_match('/[\x00-\x1F\x7F]/u', $texto);
}

/** Recupera un campo POST como texto sin espacios externos (solo NORMALIZA, no escapa). */
function textoPost(string $campo): string
{
    $valor = $_POST[$campo] ?? "";
    return is_string($valor) ? trim($valor) : "";
}

/** Igual que textoPost pero para parámetros GET. */
function textoGet(string $campo): string
{
    $valor = $_GET[$campo] ?? "";
    return is_string($valor) ? trim($valor) : "";
}

/** Devuelve el entero positivo o null si el valor no es un ID válido. */
function idPositivo($valor): ?int
{
    $resultado = filter_var($valor, FILTER_VALIDATE_INT, ["options" => ["min_range" => 1]]);
    return $resultado === false ? null : $resultado;
}

/* ----------------------------------------------------------- navegación y mensajes */

function redirigir(string $url): void
{
    header("Location: " . $url);
    exit;
}

/** Guarda un mensaje para mostrarlo en la próxima página. $tipo: exito | error | aviso */
function flash(string $tipo, string $mensaje): void
{
    $_SESSION["flash"][] = ["tipo" => $tipo, "mensaje" => $mensaje];
}

/**
 * HTML de una alerta Bootstrap. $tipo: exito | error | aviso | info.
 * El mensaje siempre se escapa con e(). Solo se aceptan los 4 tipos conocidos.
 */
function alertaHtml(string $tipo, string $mensaje, bool $cerrable = true): string
{
    $tipos = [
        "exito" => ["success", "bi-check-circle-fill"],
        "error" => ["danger",  "bi-exclamation-triangle-fill"],
        "aviso" => ["warning", "bi-exclamation-circle-fill"],
        "info"  => ["info",    "bi-info-circle-fill"],
    ];
    [$clase, $icono] = $tipos[$tipo] ?? $tipos["info"];

    $html  = '<div class="alert alert-' . $clase . ($cerrable ? ' alert-dismissible' : '') . ' fade show d-flex align-items-center" role="alert">';
    $html .= '<i class="bi ' . $icono . ' me-2 flex-shrink-0" aria-hidden="true"></i>';
    $html .= '<div>' . e($mensaje) . '</div>';
    if ($cerrable) {
        $html .= '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>';
    }
    return $html . '</div>';
}

/** Muestra los mensajes flash guardados en la sesión (una sola vez) y los elimina. */
function mostrarFlash(): void
{
    foreach ($_SESSION["flash"] ?? [] as $f) {
        echo alertaHtml((string) $f["tipo"], (string) $f["mensaje"]);
    }
    unset($_SESSION["flash"]);
}

/** Tarjeta (card) de indicador para el dashboard. $color: primary | success | warning | danger | info. */
function tarjetaIndicador(string $titulo, string $valor, string $icono, string $color, string $nota = ""): string
{
    if (!in_array($color, ["primary", "success", "warning", "danger", "info"], true)) {
        $color = "primary";
    }
    $html  = '<div class="card border-0 shadow-sm rounded-4 h-100"><div class="card-body d-flex align-items-center gap-3 p-4">';
    $html .= '<div class="icono-indicador bg-' . $color . '-subtle text-' . $color . '-emphasis"><i class="bi ' . e($icono) . '" aria-hidden="true"></i></div>';
    $html .= '<div class="min-w-0"><div class="text-body-secondary small text-uppercase fw-semibold">' . e($titulo) . '</div>';
    $html .= '<div class="indicador-valor">' . e($valor) . '</div>';
    if ($nota !== "") {
        $html .= '<div class="text-body-secondary small">' . e($nota) . '</div>';
    }
    return $html . '</div></div></div>';
}

/** Guarda errores y datos escritos para volver a mostrar el formulario. */
function guardarFormulario(array $errores, array $datos): void
{
    $_SESSION["form_errores"] = $errores;
    $_SESSION["form_viejo"]   = $datos;
}

function recuperarFormulario(): array
{
    $resultado = [
        "errores" => $_SESSION["form_errores"] ?? [],
        "viejo"   => $_SESSION["form_viejo"] ?? [],
    ];
    unset($_SESSION["form_errores"], $_SESSION["form_viejo"]);
    return $resultado;
}

/* ----------------------------------------------------------- protección CSRF */

function tokenCsrf(): string
{
    if (empty($_SESSION["csrf"])) {
        $_SESSION["csrf"] = bin2hex(random_bytes(32));
    }
    return $_SESSION["csrf"];
}

function campoCsrf(): string
{
    return '<input type="hidden" name="csrf" value="' . e(tokenCsrf()) . '">';
}

function csrfValido(): bool
{
    $token = textoPost("csrf");
    return $token !== "" && hash_equals(tokenCsrf(), $token);
}

/* ----------------------------------------------------------- clientes */

/** Lee y normaliza los datos del formulario de cliente (sin escapar). */
function leerDatosCliente(): array
{
    return [
        "nombre" => textoPost("nombre"),
        "email"  => textoPost("email"),
        "ciudad" => textoPost("ciudad"),
    ];
}

/** Valida los datos de un cliente. Devuelve un arreglo campo => mensaje (vacío si está todo bien). */
function validarCliente(array $datos): array
{
    $errores = [];

    // Codificación válida y sin caracteres de control
    foreach (["nombre", "email", "ciudad"] as $campo) {
        if (!textoSeguro($datos[$campo])) {
            $errores[$campo] = "El campo contiene caracteres no permitidos.";
        }
    }

    if (!isset($errores["nombre"])) {
        $largo = mb_strlen($datos["nombre"]);
        if ($datos["nombre"] === "") {
            $errores["nombre"] = "El nombre es obligatorio.";
        } elseif ($largo < 2 || $largo > 100) {
            $errores["nombre"] = "El nombre debe tener entre 2 y 100 caracteres.";
        }
    }

    if (!isset($errores["email"]) && $datos["email"] !== "") {
        if (mb_strlen($datos["email"]) > 120) {
            $errores["email"] = "El email no puede superar los 120 caracteres.";
        } elseif (!filter_var($datos["email"], FILTER_VALIDATE_EMAIL)) {
            $errores["email"] = "El email no tiene un formato válido.";
        }
    }

    if (!isset($errores["ciudad"]) && mb_strlen($datos["ciudad"]) > 80) {
        $errores["ciudad"] = "La ciudad no puede superar los 80 caracteres.";
    }

    return $errores;
}

/* ----------------------------------------------------------- formato y utilidades */

function dinero($monto): string
{
    return "$ " . number_format((float) $monto, 2, ",", ".");
}

function fechaLegible(string $fechaSql): string
{
    $tiempo = strtotime($fechaSql);
    return $tiempo === false ? $fechaSql : date("d/m/Y", $tiempo);
}

/** Cantidad de filas de una tabla permitida; null si la tabla no existe. */
function contarRegistros(PDO $pdo, string $tabla): ?int
{
    if (!in_array($tabla, ["clientes", "pedidos"], true)) {
        return null;
    }
    try {
        return (int) $pdo->query("SELECT COUNT(*) FROM {$tabla}")->fetchColumn();
    } catch (PDOException $e) {
        error_log("[gestion_clientes] No se pudo contar {$tabla}: " . $e->getMessage());
        return null;
    }
}

/* ----------------------------------------------------------- paginación reutilizable */

function urlConFiltros(string $archivo, array $filtros, int $pagina): string
{
    return $archivo . "?" . http_build_query($filtros + ["pagina" => $pagina]);
}

/** Devuelve el HTML (Bootstrap) de la navegación entre páginas, conservando los filtros. */
function htmlPaginacion(string $archivo, array $filtros, int $pagina, int $totalPaginas): string
{
    if ($totalPaginas <= 1) {
        return "";
    }

    $item = function (string $texto, ?int $destino, string $etiqueta = "") use ($archivo, $filtros): string {
        $aria = $etiqueta !== "" ? ' aria-label="' . e($etiqueta) . '"' : "";
        if ($destino === null) {
            return '<li class="page-item disabled"><span class="page-link"' . $aria . '>' . $texto . '</span></li>';
        }
        return '<li class="page-item"><a class="page-link" href="' . e(urlConFiltros($archivo, $filtros, $destino)) . '"' . $aria . '>' . $texto . '</a></li>';
    };

    $html  = '<nav class="mt-3" aria-label="Paginación de resultados">';
    $html .= '<ul class="pagination pagination-sm justify-content-center flex-wrap mb-2">';
    $html .= $item("&laquo;", $pagina > 1 ? 1 : null, "Primera página");
    $html .= $item("Anterior", $pagina > 1 ? $pagina - 1 : null);

    $desde = max(1, $pagina - 2);
    $hasta = min($totalPaginas, $pagina + 2);
    for ($n = $desde; $n <= $hasta; $n++) {
        $html .= ($n === $pagina)
            ? '<li class="page-item active" aria-current="page"><span class="page-link">' . $n . '</span></li>'
            : $item((string) $n, $n);
    }

    $html .= $item("Siguiente", $pagina < $totalPaginas ? $pagina + 1 : null);
    $html .= $item("&raquo;", $pagina < $totalPaginas ? $totalPaginas : null, "Última página");
    $html .= '</ul>';
    $html .= '<p class="text-center text-body-secondary small mb-0">Página ' . $pagina . ' de ' . $totalPaginas . '</p>';
    return $html . '</nav>';
}
