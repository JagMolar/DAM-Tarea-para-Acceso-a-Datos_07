CREATE TABLE IF NOT EXISTS matriculas (
  DNI varchar(9) NOT NULL,
  NombreModulo varchar(60) NOT NULL,
  Curso varchar(5) NOT NULL,
  Nota double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

--
-- Datos tabla matriculas
--

INSERT INTO matriculas (DNI, NombreModulo, Curso, Nota) VALUES
('12345678A', 'Acceso a datos', '21-22', 8),
('12345678A', 'Desarrollo de Interfaces', '21-22', 9.5),
('14785236d', 'Acceso a datos', '21-22', 7),
('14785236d', 'Desarrollo de Interfaces', '21-22', 7.5);
