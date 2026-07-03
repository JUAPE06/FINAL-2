USE giann;
GO

-- Eliminar tablas si existen (en orden inverso por FK para evitar errores de restricción)
IF OBJECT_ID('dbo.Deuda', 'U') IS NOT NULL DROP TABLE dbo.Deuda;
IF OBJECT_ID('dbo.Docente', 'U') IS NOT NULL DROP TABLE dbo.Docente;
GO

-- Tabla Docente
CREATE TABLE dbo.Docente (
    [idDocente] INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [DNI] NVARCHAR(8) NOT NULL UNIQUE,
    [Nombres] NVARCHAR(100) NOT NULL,
    [Apellidos] NVARCHAR(100) NOT NULL,
    [Especialidad] NVARCHAR(100) NULL,
    [Condicion] NVARCHAR(50) NULL -- Nombrado, Contratado
);
GO

-- Tabla Deuda
CREATE TABLE dbo.Deuda (
    [idDeuda] INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [idDocente] INT NOT NULL,
    [Concepto] NVARCHAR(255) NOT NULL,
    [Monto] DECIMAL(10, 2) NOT NULL,
    [FechaRegistro] DATE DEFAULT GETDATE(),
    [Estado] NVARCHAR(20) DEFAULT 'Pendiente', -- Pendiente, Pagado, En Proceso
    CONSTRAINT FK_Deuda_Docente
        FOREIGN KEY ([idDocente])
        REFERENCES dbo.Docente([idDocente])
        ON DELETE CASCADE
);
GO
