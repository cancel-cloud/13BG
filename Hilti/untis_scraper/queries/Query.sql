SELECT COUNT(*) AS approx_cancellations_by_KARS
FROM substitutions
WHERE raw_json LIKE '%ROHA%'
  AND raw_json LIKE '%Entfall%';
