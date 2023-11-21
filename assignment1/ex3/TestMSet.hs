import MultiSet
-- import GHC.IO
import Data.Char (toLower, isAlpha)
import Data.List (sort)

ciao str = sort $ map toLower str

readMSet path = do
      words <- fmap lines (readFile path)
      let ciaos = map ciao words
      -- print $ take 10 ciaos --TODO remove
      return $ addList empty ciaos

-- Define a function writeMSet that given a multiset and a file name, writes in the file, one
-- per line, each element of the multiset with its multiplicity in the format “<elem> - <multiplicity>”
writeMSet :: Show a => MSet a -> FilePath -> IO ()
writeMSet (MS mset) path = do
   let lines = [show x ++ " - " ++ show m | (x,m) <- mset]
   writeFile path (unlines lines)

main = do
   m1 <- readMSet "../aux_files/anagram.txt"
   m2 <- readMSet "../aux_files/anagram-s1.txt"
   m3 <- readMSet "../aux_files/anagram-s2.txt"
   m4 <- readMSet "../aux_files/margana2.txt"

   -- Multisets m1 and m4 are not equal, 
   -- but they have the same elements;
   print $ "m1 == m4 :" ++ show (m1 == m4)
   print $ "elems m1 == elems m4 :" ++ show (elems m1 == elems m4)

   -- Multiset m1 is equal to the union 
   -- of multisets m2 and m3;
   let m23 = m2 `union` m3
   print $ "m1 == union m2 m3 :" ++ show (m1 == union m2 m3)
   -- print $ "diff m1 (union m2 m3) :" ++ show (diff m1 m23)
   -- print $ "diff m1 (union m2 m3) :" ++ show (diff' m1 m23)


   writeMSet m1 "./output/m1.txt"
   writeMSet m2 "./output/m2.txt"
   writeMSet m3 "./output/m3.txt"
