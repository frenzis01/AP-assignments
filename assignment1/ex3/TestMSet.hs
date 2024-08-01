import MultiSet
import Data.Char (toLower, isAlpha)
import Data.List (sort)

-- | Computes the ciao of a single string
-- first maps every char to lowercase,
-- then sorts them
-- 
-- @param str string to compute the ciao
-- @returns ciao of str
ciao :: [Char] -> [Char]
ciao str = sort $ map toLower str

-- Computes the ciao of every word inside a text file,
-- assuming that the words are newline-separated,
-- and then returns a MultiSet in containing the ciaos
-- 
-- @param path file to be read containing newline separated words
-- @returns MultiSet of ciao of words inside a read file
readMSet :: FilePath -> IO (MSet [Char])
readMSet path = do
      words <- fmap lines (readFile path)
      let ciaos = map ciao words
      return $ addList empty ciaos

-- Define a function writeMSet that given a multiset and a file name, writes in the file, one
-- per line, each element of the multiset with its multiplicity in the format “<elem> - <multiplicity>”
writeMSet :: Show a => MSet a -> FilePath -> IO ()
writeMSet (MS mset) path = do
   let lines = [show x ++ " - " ++ show m | (x,m) <- mset]
   writeFile path (unlines lines)

main :: IO ()
main = do
   m1 <- readMSet "../aux_files/anagram.txt"
   m2 <- readMSet "../aux_files/anagram-s1.txt"
   m3 <- readMSet "../aux_files/anagram-s2.txt"
   m4 <- readMSet "../aux_files/margana2.txt"

   -- Test that:
   --    Multisets m1 and m4 are not equal, 
   --    but they have the same elements;
   let res = m1 == m4
   print $ "m1 == m4 :" ++ show (m1 == m4) ++ " < expected False"
   print $ "elems m1 == elems m4 :" ++ show (elems m1 == elems m4)  ++ " < expected True"

   -- Test that:
   --    Multiset m1 is equal to the union 
   --    of multisets m2 and m3;
   let m23 = m2 `union` m3
   print $ "m1 == union m2 m3 :" ++ show (m1 == union m2 m3)  ++ " < expected True"

   -- Optional debug write
   --    writeMSet m1 "./output/m1.txt"
   --    writeMSet m2 "./output/m2.txt"
   --    writeMSet m3 "./output/m3.txt"
