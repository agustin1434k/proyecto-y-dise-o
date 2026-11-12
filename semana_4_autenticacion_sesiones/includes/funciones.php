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

/** Escapa un valor para mostrarlo en HTML (solo para SALIDA). */
function e($valor): string
{
    return htmlspecialchars((string) $valor, ENT_QUOTES, "UTF-8");
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

function mostrarFlash(): void
{
    foreach ($_SESSION["flash"] ?? [] as $f) {
        echo '<div class="alerta alerta-' . e($f["tipo"]) . '">' . e($f["mensaje"]) . '</div>';
    }
    unset($_SESSION["flash"]);
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

    $largo = mb_strlen($datos["nombre"]);
    if ($datos["nombre"] === "") {
        $errores["nombre"] = "El nombre es obligatorio.";
    } elseif ($largo < 2 || $largo > 100) {
        $errores["nombre"] = "El nombre debe tener entre 2 y 100 caracteres.";
    }

    if ($datos["email"] !== "") {
        if (mb_strlen($datos["email"]) > 120) {
            $errores["email"] = "El email no puede superar los 120 caracteres.";
        } elseif (!filter_var($datos["email"], FILTER_VALIDATE_EMAIL)) {
            $errores["email"] = "El email no tiene un formato válido.";
        }
    }

    if (mb_strlen($datos["ciudad"]) > 80) {
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

/** Devuelve el HTML de la navegación entre páginas, conservando los filtros. */
function htmlPaginacion(string $archivo, array $filtros, int $pagina, int $totalPaginas): string
{
    if ($totalPaginas <= 1) {
        return "";
    }

    $item = function (string $texto, ?int $destino) use ($archivo, $filtros): string {
        if ($destino === null) {
            return '<span class="pag desactivada">' . $texto . '</span>';
        }
        return '<a class="pag" href="' . e(urlConFiltros($archivo, $filtros, $destino)) . '">' . $texto . '</a>';
    };

    $html  = '<nav class="paginacion" aria-label="Paginación">';
    $html .= $item("&laquo; Primera", $pagina > 1 ? 1 : null);
    $html .= $item("&lt;&lt; Anterior", $pagina > 1 ? $pagina - 1 : null);

    $desde = max(1, $pagina - 2);
    $hasta = min($totalPaginas, $pagina + 2);
    for ($n = $desde; $n <= $hasta; $n++) {
        $html .= ($n === $pagina)
            ? '<span class="pag activa">' . $n . '</span>'
            : $item((string) $n, $n);
    }

    $html .= $item("Siguiente &gt;&gt;", $pagina < $totalPaginas ? $pagina + 1 : null);
    $html .= $item("Última &raquo;", $pagina < $totalPaginas ? $totalPaginas : null);
    $html .= '<div class="info-pagina">Página ' . $pagina . ' de ' . $totalPaginas . '</div>';
    $html .= '</nav>';
    return $html;
}
