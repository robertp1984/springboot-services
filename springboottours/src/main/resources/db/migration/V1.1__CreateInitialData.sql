CREATE OR REPLACE FUNCTION add_tour_package(code VARCHAR, name VARCHAR) RETURNS NUMERIC AS $$
    BEGIN
        INSERT INTO TOUR_PACKAGE(id, code, name) values(nextval('TOUR_PACKAGE_SEQ'), code, name);
        RETURN currval('tour_package_seq');
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION add_tour(code VARCHAR, name VARCHAR, description VARCHAR, difficulty VARCHAR, region VARCHAR, tour_package_code VARCHAR) RETURNS NUMERIC AS $$
    DECLARE
        tour_package_id NUMERIC;
    BEGIN
        SELECT tp.id INTO tour_package_id FROM TOUR_PACKAGE tp WHERE tp.code=tour_package_code;
        INSERT INTO TOUR(id, code, name, description, difficulty, region, tour_package_id) VALUES(nextval('tour_seq'), code, name, description, difficulty, region, tour_package_id);
        RETURN currval('tour_seq');
    END;
$$ LANGUAGE plpgsql;



BEGIN;

-- Initial data for tour package
SELECT add_tour_package(
SELECT add_tour_package('BC', 'Backpack Cal');
SELECT add_tour_package('CC', 'California Calm');
SELECT add_tour_package('CH', 'California Hot springs');
SELECT add_tour_package('CY', 'Cycle California');
SELECT add_tour_package('DS', 'From Desert to Sea');
SELECT add_tour_package('KC', 'Kids California');
SELECT add_tour_package('NW', 'Nature Watch');
SELECT add_tour_package('SC', 'Snowboard Cali');
SELECT add_tour_package('TC', 'Taste of California');
SELECT add_tour_package('EU', 'Taste of Europe');

-- Initial data for tour
SELECT add_tour('POL', 'Poland', 'Poland is a country located in Central Europe. It is known for its rich history, beautiful landscapes, and vibrant culture.',
'EASY', 'CENTRAL_EUROPE', 'EU');
SELECT add_tour('ITA', 'Italy', 'Italy is a country in Southern Europe known for its rich history, art, culture, and cuisine. It is home to many famous landmarks such as the Colosseum, Leaning Tower of Pisa, and Venice canals.',
'MEDIUM', 'SOUTHERN_EUROPE', 'EU');
SELECT add_tour('ESP', 'Spain', 'Spain is a country located in Southwestern Europe. It is known for its diverse culture, beautiful beaches, historic cities, and delicious cuisine.',
'EASY', 'SOUTHERN_EUROPE', 'EU');

END;


